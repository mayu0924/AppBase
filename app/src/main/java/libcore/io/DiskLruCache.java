/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package libcore.io;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 ****************************************************************************** 
 * Taken from the JB source code, can be found in:
 * libcore/luni/src/main/java/libcore/io/DiskLruCache.java or direct link:
 * https://android.googlesource.com/platform/libcore/+/android-4.1
 * .1_r1/luni/src/main/java/libcore/io/DiskLruCache.java
 ****************************************************************************** 
 * 
 * A cache that uses a bounded amount of space on a filesystem. Each cache entry
 * has a string key and a fixed number of values. Values are byte sequences,
 * accessible as streams or files. Each value must be between {@code 0} and
 * {@code Integer.MAX_VALUE} bytes in length.
 * 一个使用空间大小有边界的文件cache，每一个entry包含一个key和values。values是byte序列，按文件或者流来访问的。
 * 每一个value的长度在0---Integer.MAX_VALUE之间。
 * 
 * <p>
 * The cache stores its data in a directory on the filesystem. This directory
 * must be exclusive to the cache; the cache may delete or overwrite files from
 * its directory. It is an error for multiple processes to use the same cache
 * directory at the same time.
 * cache使用目录文件存储数据。文件路径必须是唯一的，可以删除和重写目录文件。多个进程同时使用同样的文件目录是不正确的
 * 
 * <p>
 * This cache limits the number of bytes that it will store on the filesystem.
 * When the number of stored bytes exceeds the limit, the cache will remove
 * entries in the background until the limit is satisfied. The limit is not
 * strict: the cache may temporarily exceed it while waiting for files to be
 * deleted. The limit does not include filesystem overhead or the cache journal
 * so space-sensitive applications should set a conservative limit.
 * cache限制了大小，当超出空间大小时，cache就会后台删除entry直到空间没有达到上限为止。空间大小限制不是严格的，
 * cache可能会暂时超过limit在等待文件删除的过程中。cache的limit不包括文件系统的头部和日志，
 * 所以空间大小敏感的应用应当设置一个保守的limit大小
 * 
 * <p>
 * Clients call {@link #edit} to create or update the values of an entry. An
 * entry may have only one editor at one time; if a value is not available to be
 * edited then {@link #edit} will return null.
 * <ul>
 * <li>When an entry is being <strong>created</strong> it is necessary to supply
 * a full set of values; the empty value should be used as a placeholder if
 * necessary.
 * <li>When an entry is being <strong>edited</strong>, it is not necessary to
 * supply data for every value; values default to their previous value.
 * </ul>
 * Every {@link #edit} call must be matched by a call to {@link Editor#commit}
 * or {@link Editor#abort}. Committing is atomic: a read observes the full set
 * of values as they were before or after the commit, but never a mix of values.
 * 调用edit（）来创建或者更新entry的值，一个entry同时只能有一个editor；如果值不可被编辑就返回null。
 * 当entry被创建时必须提供一个value。空的value应当用占位符表示。当entry被编辑的时候，必须提供value。 每次调用必须有匹配Editor
 * commit或abort，commit是原子操作，读必须在commit前或者后，不会造成值混乱。
 * 
 * <p>
 * Clients call {@link #get} to read a snapshot of an entry. The read will
 * observe the value at the time that {@link #get} was called. Updates and
 * removals after the call do not impact ongoing reads.
 * 调用get来读entry的快照。当get调用时读者读其值，更新或者删除不会影响先前的读
 * 
 * <p>
 * This class is tolerant of some I/O errors. If files are missing from the
 * filesystem, the corresponding entries will be dropped from the cache. If an
 * error occurs while writing a cache value, the edit will fail silently.
 * Callers should handle other problems by catching {@code IOException} and
 * responding appropriately. 该类可以容忍一些I/O
 * errors。如果文件丢失啦，相应的entry就会被drop。写cache时如果error发生，edit将失败。 调用者应当相应的处理其它问题
 */
public final class DiskLruCache implements Closeable {
	static final String JOURNAL_FILE = "journal";
	static final String JOURNAL_FILE_TMP = "journal.tmp";
	static final String MAGIC = "libcore.io.DiskLruCache";
	static final String VERSION_1 = "1";
	static final long ANY_SEQUENCE_NUMBER = -1;
	private static final String CLEAN = "CLEAN";
	private static final String DIRTY = "DIRTY";
	private static final String REMOVE = "REMOVE";
	private static final String READ = "READ";

	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final int IO_BUFFER_SIZE = 8 * 1024;

	/*
	 * This cache uses a journal file named "journal". A typical journal file
	 * looks like this: libcore.io.DiskLruCache 1 100 2
	 * 
	 * CLEAN 3400330d1dfc7f3f7f4b8d4d803dfcf6 832 21054 DIRTY
	 * 335c4c6028171cfddfbaae1a9c313c52 CLEAN 335c4c6028171cfddfbaae1a9c313c52
	 * 3934 2342 REMOVE 335c4c6028171cfddfbaae1a9c313c52 DIRTY
	 * 1ab96a171faeeee38496d8b330771a7a CLEAN 1ab96a171faeeee38496d8b330771a7a
	 * 1600 234 READ 335c4c6028171cfddfbaae1a9c313c52 READ
	 * 3400330d1dfc7f3f7f4b8d4d803dfcf6
	 * 
	 * The first five lines of the journal form its header. They are the
	 * constant string "libcore.io.DiskLruCache", the disk cache's version, the
	 * application's version, the value count, and a blank line.
	 * 
	 * Each of the subsequent lines in the file is a record of the state of a
	 * cache entry. Each line contains space-separated values: a state, a key,
	 * and optional state-specific values. o DIRTY lines track that an entry is
	 * actively being created or updated. Every successful DIRTY action should
	 * be followed by a CLEAN or REMOVE action. DIRTY lines without a matching
	 * CLEAN or REMOVE indicate that temporary files may need to be deleted. o
	 * CLEAN lines track a cache entry that has been successfully published and
	 * may be read. A publish line is followed by the lengths of each of its
	 * values. o READ lines track accesses for LRU. o REMOVE lines track entries
	 * that have been deleted.
	 * 
	 * The journal file is appended to as cache operations occur. The journal
	 * may occasionally be compacted by dropping redundant lines. A temporary
	 * file named "journal.tmp" will be used during compaction; that file should
	 * be deleted if it exists when the cache is opened.
	 */

	private final File directory;
	private final File journalFile;// 日志文件
	private final File journalFileTmp;// 日志文件临时文件
	private final int appVersion;// 应用Version
	private final long maxSize;// 最大空间
	private final int valueCount;// key对应的value的个数
	private long size = 0;
	private Writer journalWriter;
	private final LinkedHashMap<String, Entry> lruEntries = new LinkedHashMap<String, Entry>(
			0, 0.75f, true);
	private int redundantOpCount;

	/**
	 * To differentiate between old and current snapshots, each entry is given a
	 * sequence number each time an edit is committed. A snapshot is stale if
	 * its sequence number is not equal to its entry's sequence number.
	 * 区分老的和当前的快照，每一个实体在每次编辑被committed时都被赋予一个序列号。 一个快照的序列号如果不等于entry的序列号那它就是废弃的。
	 */
	private long nextSequenceNumber = 0;

	/* From java.util.Arrays 数组拷贝 */
	@SuppressWarnings("unchecked")
	private static <T> T[] copyOfRange(T[] original, int start, int end) {
		final int originalLength = original.length; // For exception priority
													// compatibility.
		if (start > end) {
			throw new IllegalArgumentException();
		}
		if (start < 0 || start > originalLength) {
			throw new ArrayIndexOutOfBoundsException();
		}
		final int resultLength = end - start;
		final int copyLength = Math.min(resultLength, originalLength - start);
		final T[] result = (T[]) Array.newInstance(original.getClass()
				.getComponentType(), resultLength);
		System.arraycopy(original, start, result, 0, copyLength);
		return result;
	}

	/**
	 * Returns the remainder of 'reader' as a string, closing it when done.
	 * 返回String的值，然后close
	 */
	public static String readFully(Reader reader) throws IOException {
		try {
			StringWriter writer = new StringWriter();
			char[] buffer = new char[1024];
			int count;
			while ((count = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, count);
			}
			return writer.toString();
		} finally {
			reader.close();
		}
	}

	/**
	 * Returns the ASCII characters up to but not including the next "\r\n", or
	 * "\n".
	 * 
	 * @throws EOFException
	 *             if the stream is exhausted before the next newline character.
	 *             读取输入流中返回的某行ASCII码字符
	 */
	public static String readAsciiLine(InputStream in) throws IOException {
		// TODO: support UTF-8 here instead

		StringBuilder result = new StringBuilder(80);
		while (true) {
			int c = in.read();
			if (c == -1) {
				throw new EOFException();
			} else if (c == '\n') {
				break;
			}

			result.append((char) c);
		}
		int length = result.length();
		if (length > 0 && result.charAt(length - 1) == '\r') {
			result.setLength(length - 1);
		}
		return result.toString();
	}

	/**
	 * Closes 'closeable', ignoring any checked exceptions. Does nothing if
	 * 'closeable' is null. closeable关闭
	 */
	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (RuntimeException rethrown) {
				throw rethrown;
			} catch (Exception ignored) {
			}
		}
	}

	/**
	 * Recursively delete everything in {@code dir}. 递归删除dir
	 */
	// TODO: this should specify paths as Strings rather than as Files
	public static void deleteContents(File dir) throws IOException {
		File[] files = dir.listFiles();
		if (files == null) {
			throw new IllegalArgumentException("not a directory: " + dir);
		}
		for (File file : files) {
			if (file.isDirectory()) {
				deleteContents(file);
			}
			if (!file.delete()) {
				throw new IOException("failed to delete file: " + file);
			}
		}
	}

	/**
	 * This cache uses a single background thread to evict entries. 后台单线程回收entry
	 */
	private final ExecutorService executorService = new ThreadPoolExecutor(0,
			1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	private final Callable<Void> cleanupCallable = new Callable<Void>() {
		@Override
		public Void call() throws Exception {
			synchronized (DiskLruCache.this) {
				if (journalWriter == null) {
					return null; // closed
				}
				trimToSize();
				if (journalRebuildRequired()) {
					rebuildJournal();
					redundantOpCount = 0;
				}
			}
			return null;
		}
	};

	// 构造器
	private DiskLruCache(File directory, int appVersion, int valueCount,
			long maxSize) {
		this.directory = directory;
		this.appVersion = appVersion;
		this.journalFile = new File(directory, JOURNAL_FILE);
		this.journalFileTmp = new File(directory, JOURNAL_FILE_TMP);
		this.valueCount = valueCount;
		this.maxSize = maxSize;
	}

	/**
	 * 
	 * step1: 打开指定目录(directory)的缓存，如果还不存在就会创建。 Opens the cache in
	 * {@code directory}, creating a cache if none exists there.
	 * 
	 * @param directory
	 *            数据的缓存目录
	 * @param appVersion
	 *            应用程序的版本号
	 * @param valueCount
	 *            同一个key可以对应多少个缓存文件，一般都是传1
	 * @param maxSize
	 *            最多可以缓存多少字节的数据
	 * @throws IOException
	 *             if reading or writing the cache directory fails
	 */
	public static DiskLruCache open(File directory, int appVersion,
			int valueCount, long maxSize) throws IOException {
		if (maxSize <= 0) {
			throw new IllegalArgumentException("maxSize <= 0");
		}
		if (valueCount <= 0) {
			throw new IllegalArgumentException("valueCount <= 0");
		}

		// prefer to pick up where we left off 优先处理先前的cache
		DiskLruCache cache = new DiskLruCache(directory, appVersion,
				valueCount, maxSize);
		if (cache.journalFile.exists()) {
			try {
				cache.readJournal();
				cache.processJournal();
				cache.journalWriter = new BufferedWriter(new FileWriter(
						cache.journalFile, true), IO_BUFFER_SIZE);
				return cache;
			} catch (IOException journalIsCorrupt) {
				// System.logW("DiskLruCache " + directory + " is corrupt: "
				// + journalIsCorrupt.getMessage() + ", removing");
				cache.delete();
			}
		}

		// create a new empty cache 创建一个空新的cache
		directory.mkdirs();
		cache = new DiskLruCache(directory, appVersion, valueCount, maxSize);
		cache.rebuildJournal();
		return cache;
	}

	// 读取日志信息
	private void readJournal() throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(
				journalFile), IO_BUFFER_SIZE);
		try {
			String magic = readAsciiLine(in);
			String version = readAsciiLine(in);
			String appVersionString = readAsciiLine(in);
			String valueCountString = readAsciiLine(in);
			String blank = readAsciiLine(in);
			if (!MAGIC.equals(magic) || !VERSION_1.equals(version)
					|| !Integer.toString(appVersion).equals(appVersionString)
					|| !Integer.toString(valueCount).equals(valueCountString)
					|| !"".equals(blank)) {
				throw new IOException("unexpected journal header: [" + magic
						+ ", " + version + ", " + valueCountString + ", "
						+ blank + "]");
			}

			while (true) {
				try {
					readJournalLine(readAsciiLine(in));
					;// 读取日志信息
				} catch (EOFException endOfJournal) {
					break;
				}
			}
		} finally {
			closeQuietly(in);// 关闭输入流
		}
	}

	// 读取日志中某行日志信息
	private void readJournalLine(String line) throws IOException {
		String[] parts = line.split(" ");
		if (parts.length < 2) {
			throw new IOException("unexpected journal line: " + line);
		}

		String key = parts[1];
		if (parts[0].equals(REMOVE) && parts.length == 2) {
			lruEntries.remove(key);
			return;
		}

		Entry entry = lruEntries.get(key);
		if (entry == null) {
			entry = new Entry(key);
			lruEntries.put(key, entry);
		}

		if (parts[0].equals(CLEAN) && parts.length == 2 + valueCount) {
			entry.readable = true;
			entry.currentEditor = null;
			entry.setLengths(copyOfRange(parts, 2, parts.length));
		} else if (parts[0].equals(DIRTY) && parts.length == 2) {
			entry.currentEditor = new Editor(entry);
		} else if (parts[0].equals(READ) && parts.length == 2) {
			// this work was already done by calling lruEntries.get()
		} else {
			throw new IOException("unexpected journal line: " + line);
		}
	}

	/**
	 * Computes the initial size and collects garbage as a part of opening the
	 * cache. Dirty entries are assumed to be inconsistent and will be deleted.
	 * 处理日志 计算初始化cache的初始化大小和收集垃圾。Dirty entry假定不一致将会被删掉。
	 */
	private void processJournal() throws IOException {
		deleteIfExists(journalFileTmp);// 删除日志文件
		for (Iterator<Entry> i = lruEntries.values().iterator(); i.hasNext();) {
			Entry entry = i.next();
			if (entry.currentEditor == null) {
				for (int t = 0; t < valueCount; t++) {
					size += entry.lengths[t];
				}
			} else {
				entry.currentEditor = null;
				for (int t = 0; t < valueCount; t++) {
					deleteIfExists(entry.getCleanFile(t));
					deleteIfExists(entry.getDirtyFile(t));
				}
				i.remove();
			}
		}
	}

	/**
	 * Creates a new journal that omits redundant information. This replaces the
	 * current journal if it exists. 创建一个新的删掉冗余信息的日志。替换当前的日志
	 */
	private synchronized void rebuildJournal() throws IOException {
		if (journalWriter != null) {
			journalWriter.close();
		}

		Writer writer = new BufferedWriter(new FileWriter(journalFileTmp),
				IO_BUFFER_SIZE);
		writer.write(MAGIC);
		writer.write("\n");
		writer.write(VERSION_1);
		writer.write("\n");
		writer.write(Integer.toString(appVersion));
		writer.write("\n");
		writer.write(Integer.toString(valueCount));
		writer.write("\n");
		writer.write("\n");

		for (Entry entry : lruEntries.values()) {
			if (entry.currentEditor != null) {
				writer.write(DIRTY + ' ' + entry.key + '\n');
			} else {
				writer.write(CLEAN + ' ' + entry.key + entry.getLengths()
						+ '\n');
			}
		}

		writer.close();
		journalFileTmp.renameTo(journalFile);
		journalWriter = new BufferedWriter(new FileWriter(journalFile, true),
				IO_BUFFER_SIZE);
	}

	// 文件若存在删除
	private static void deleteIfExists(File file) throws IOException {
		// try {
		// Libcore.os.remove(file.getPath());
		// } catch (ErrnoException errnoException) {
		// if (errnoException.errno != OsConstants.ENOENT) {
		// throw errnoException.rethrowAsIOException();
		// }
		// }
		if (file.exists() && !file.delete()) {
			throw new IOException();
		}
	}

	/**
	 * step1: 根据key来获取到相应的缓存快照对象 Returns a snapshot of the entry named
	 * {@code key}, or null if it doesn't exist is not currently readable. If a
	 * value is returned, it is moved to the head of the LRU queue.
	 * 返回key对应的entry的snapshot，当key相应的entry不存在或者当前不可读时返回null。
	 * 如果返回相应的值，它就会被移动到LRU队列的头部。
	 */
	public synchronized Snapshot get(String key) throws IOException {
		checkNotClosed();// 检查cache是否已关闭
		validateKey(key);// 验证key格式的正确性
		Entry entry = lruEntries.get(key);
		if (entry == null) {
			return null;
		}

		if (!entry.readable) {
			return null;
		}

		/*
		 * Open all streams eagerly to guarantee that we see a single published
		 * snapshot. If we opened streams lazily then the streams could come
		 * from different edits.
		 */
		InputStream[] ins = new InputStream[valueCount];
		try {
			for (int i = 0; i < valueCount; i++) {
				ins[i] = new FileInputStream(entry.getCleanFile(i));
			}
		} catch (FileNotFoundException e) {
			// a file must have been deleted manually!
			return null;
		}

		redundantOpCount++;
		journalWriter.append(READ + ' ' + key + '\n');
		if (journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}

		return new Snapshot(key, entry.sequenceNumber, ins);
	}

	/**
	 * step2: 返回针对指定key的缓存编辑器 Returns an editor for the entry named {@code key},
	 * or null if another edit is in progress.
	 */
	public Editor edit(String key) throws IOException {
		return edit(key, ANY_SEQUENCE_NUMBER);
	}

	private synchronized Editor edit(String key, long expectedSequenceNumber)
			throws IOException {
		checkNotClosed();// 检查cache关闭与否
		validateKey(key);// 验证key格式正确性
		Entry entry = lruEntries.get(key);
		if (expectedSequenceNumber != ANY_SEQUENCE_NUMBER
				&& (entry == null || entry.sequenceNumber != expectedSequenceNumber)) {
			return null; // snapshot is stale
		}
		if (entry == null) {
			entry = new Entry(key);
			lruEntries.put(key, entry);
		} else if (entry.currentEditor != null) {
			return null; // another edit is in progress
		}

		Editor editor = new Editor(entry);
		entry.currentEditor = editor;

		// flush the journal before creating files to prevent file leaks
		journalWriter.write(DIRTY + ' ' + key + '\n');
		journalWriter.flush();
		return editor;
	}

	/**
	 * Returns the directory where this cache stores its data.
	 */
	public File getDirectory() {
		return directory;
	}

	/**
	 * Returns the maximum number of bytes that this cache should use to store
	 * its data.
	 */
	public long maxSize() {
		return maxSize;
	}

	/**
	 * Returns the number of bytes currently being used to store the values in
	 * this cache. This may be greater than the max size if a background
	 * deletion is pending.
	 */
	public synchronized long size() {
		return size;
	}

	// 完成Edit动作
	private synchronized void completeEdit(Editor editor, boolean success)
			throws IOException {
		Entry entry = editor.entry;
		if (entry.currentEditor != editor) {
			throw new IllegalStateException();
		}

		// if this edit is creating the entry for the first time, every index
		// must have a value
		if (success && !entry.readable) {
			for (int i = 0; i < valueCount; i++) {
				if (!entry.getDirtyFile(i).exists()) {
					editor.abort();
					throw new IllegalStateException("edit didn't create file "
							+ i);
				}
			}
		}

		for (int i = 0; i < valueCount; i++) {
			File dirty = entry.getDirtyFile(i);
			if (success) {
				if (dirty.exists()) {
					File clean = entry.getCleanFile(i);
					dirty.renameTo(clean);
					long oldLength = entry.lengths[i];
					long newLength = clean.length();
					entry.lengths[i] = newLength;
					size = size - oldLength + newLength;
				}
			} else {
				deleteIfExists(dirty);
			}
		}

		redundantOpCount++;
		entry.currentEditor = null;
		if (entry.readable | success) {
			entry.readable = true;
			journalWriter.write(CLEAN + ' ' + entry.key + entry.getLengths()
					+ '\n');
			if (success) {
				entry.sequenceNumber = nextSequenceNumber++;
			}
		} else {
			lruEntries.remove(entry.key);
			journalWriter.write(REMOVE + ' ' + entry.key + '\n');
		}

		if (size > maxSize || journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}
	}

	/**
	 * We only rebuild the journal when it will halve the size of the journal
	 * and eliminate at least 2000 ops. 当日志大小减半并且删掉至少2000项时重新构造日志
	 */
	private boolean journalRebuildRequired() {
		final int REDUNDANT_OP_COMPACT_THRESHOLD = 2000;
		return redundantOpCount >= REDUNDANT_OP_COMPACT_THRESHOLD
				&& redundantOpCount >= lruEntries.size();
	}

	/**
	 * Drops the entry for {@code key} if it exists and can be removed. Entries
	 * actively being edited cannot be removed. 删除key相应的entry，被编辑的Entry不能被remove
	 * 
	 * @return true if an entry was removed.
	 */
	public synchronized boolean remove(String key) throws IOException {
		checkNotClosed();// 检查cache是否已经关闭
		validateKey(key);// 验证key格式的正确性
		Entry entry = lruEntries.get(key);
		if (entry == null || entry.currentEditor != null) {
			return false;
		}

		for (int i = 0; i < valueCount; i++) {
			File file = entry.getCleanFile(i);
			if (!file.delete()) {
				throw new IOException("failed to delete " + file);
			}
			size -= entry.lengths[i];
			entry.lengths[i] = 0;
		}

		redundantOpCount++;
		journalWriter.append(REMOVE + ' ' + key + '\n');
		lruEntries.remove(key);

		if (journalRebuildRequired()) {
			executorService.submit(cleanupCallable);
		}

		return true;
	}

	/**
	 * Returns true if this cache has been closed. 判断cache是否已经关闭
	 */
	public boolean isClosed() {
		return journalWriter == null;
	}

	// 检查cache是否已经关闭
	private void checkNotClosed() {
		if (journalWriter == null) {
			throw new IllegalStateException("cache is closed");
		}
	}

	/**
	 * Force buffered operations to the filesystem.
	 */
	public synchronized void flush() throws IOException {
		checkNotClosed();// 检查cache是否关闭
		trimToSize();// 满足最大空间limit
		journalWriter.flush();
	}

	/**
	 * Closes this cache. Stored values will remain on the filesystem. 关闭cache。
	 */
	public synchronized void close() throws IOException {
		if (journalWriter == null) {
			return; // already closed
		}
		for (Entry entry : new ArrayList<Entry>(lruEntries.values())) {
			if (entry.currentEditor != null) {
				entry.currentEditor.abort();
			}
		}
		trimToSize();
		journalWriter.close();
		journalWriter = null;
	}

	// 回收删除某些entry到空间大小满足maxsize
	private void trimToSize() throws IOException {
		while (size > maxSize) {
			// Map.Entry<String, Entry> toEvict = lruEntries.eldest();
			final Map.Entry<String, Entry> toEvict = lruEntries.entrySet()
					.iterator().next();
			remove(toEvict.getKey());
		}
	}

	/**
	 * Closes the cache and deletes all of its stored values. This will delete
	 * all files in the cache directory including files that weren't created by
	 * the cache. 关闭删除cache
	 */
	public void delete() throws IOException {
		close();
		deleteContents(directory);
	}

	// 验证key格式的正确性
	private void validateKey(String key) {
		if (key.contains(" ") || key.contains("\n") || key.contains("\r")) {
			throw new IllegalArgumentException(
					"keys must not contain spaces or newlines: \"" + key + "\"");
		}
	}

	// 字符串形式读出输入流的内容
	private static String inputStreamToString(InputStream in)
			throws IOException {
		return readFully(new InputStreamReader(in, UTF_8));
	}

	/**
	 * A snapshot of the values for an entry. entry的快照
	 */
	public final class Snapshot implements Closeable {
		private final String key;
		private final long sequenceNumber;// 序列号（同文件名称）
		private final InputStream[] ins;// 两个修改的文件输入流

		private Snapshot(String key, long sequenceNumber, InputStream[] ins) {
			this.key = key;
			this.sequenceNumber = sequenceNumber;
			this.ins = ins;
		}

		/**
		 * 
		 * Returns an editor for this snapshot's entry, or null if either the
		 * entry has changed since this snapshot was created or if another edit
		 * is in progress. 返回entry快照的editor，如果entry已经更新了或者另一个edit正在处理过程中返回null。
		 */
		public Editor edit() throws IOException {
			return DiskLruCache.this.edit(key, sequenceNumber);
		}

		/**
		 * step2: 创建针对指定索引(从0开始)处的文件的输入流，用于从文件中读取缓存的数据 Returns the unbuffered
		 * stream with the value for {@code index}.
		 */
		public InputStream getInputStream(int index) {
			return ins[index];
		}

		/**
		 * Returns the string value for {@code index}.
		 */
		public String getString(int index) throws IOException {
			return inputStreamToString(getInputStream(index));
		}

		@Override
		public void close() {
			for (InputStream in : ins) {
				closeQuietly(in);
			}
		}
	}

	/**
	 * Edits the values for an entry. entry编辑器
	 */
	public final class Editor {
		private final Entry entry;
		private boolean hasErrors;

		private Editor(Entry entry) {
			this.entry = entry;
		}

		/**
		 * 
		 * Returns an unbuffered input stream to read the last committed value,
		 * or null if no value has been committed.
		 * 返回一个最后提交的entry的不缓存输入流，如果没有值被提交过返回null
		 */
		public InputStream newInputStream(int index) throws IOException {
			synchronized (DiskLruCache.this) {
				if (entry.currentEditor != this) {
					throw new IllegalStateException();
				}
				if (!entry.readable) {
					return null;
				}
				return new FileInputStream(entry.getCleanFile(index));
			}
		}

		/**
		 * Returns the last committed value as a string, or null if no value has
		 * been committed. 返回最后提交的entry的文件内容，字符串形式
		 */
		public String getString(int index) throws IOException {
			InputStream in = newInputStream(index);
			return in != null ? inputStreamToString(in) : null;
		}

		/**
		 * step3: 创建指定索引(从0开始)处的输出流，用于写出要缓存的数据 Returns a new unbuffered output
		 * stream to write the value at {@code index}. If the underlying output
		 * stream encounters errors when writing to the filesystem, this edit
		 * will be aborted when {@link #commit} is called. The returned output
		 * stream does not throw IOExceptions.
		 * 
		 * @param index
		 *            ，指定索引处的缓存文件。前面就只指定了一个key对应一个缓存文件，所以一般传入0即可
		 *            返回一个新的无缓冲的输出流，写文件时如果潜在的输出流存在错误，这个edit将被废弃。
		 */
		public OutputStream newOutputStream(int index) throws IOException {
			synchronized (DiskLruCache.this) {
				if (entry.currentEditor != this) {
					throw new IllegalStateException();
				}
				return new FaultHidingOutputStream(new FileOutputStream(
						entry.getDirtyFile(index)));
			}
		}

		/**
		 * Sets the value at {@code index} to {@code value}. 设置entry的value的文件的内容
		 */
		public void set(int index, String value) throws IOException {
			Writer writer = null;
			try {
				writer = new OutputStreamWriter(newOutputStream(index), UTF_8);
				writer.write(value);
			} finally {
				closeQuietly(writer);
			}
		}

		/**
		 * Commits this edit so it is visible to readers. This releases the edit
		 * lock so another edit may be started on the same key.
		 * commit提交编辑的结果，释放edit锁然后其它edit可以启动
		 */
		public void commit() throws IOException {
			if (hasErrors) {
				completeEdit(this, false);
				remove(entry.key); // the previous entry is stale
			} else {
				completeEdit(this, true);
			}
		}

		/**
		 * Aborts this edit. This releases the edit lock so another edit may be
		 * started on the same key. 废弃edit，释放edit锁然后其它edit可以启动
		 */
		public void abort() throws IOException {
			completeEdit(this, false);
		}

		// 包装的输出流类
		private class FaultHidingOutputStream extends FilterOutputStream {
			private FaultHidingOutputStream(OutputStream out) {
				super(out);
			}

			@Override
			public void write(int oneByte) {
				try {
					out.write(oneByte);
				} catch (IOException e) {
					hasErrors = true;
				}
			}

			@Override
			public void write(byte[] buffer, int offset, int length) {
				try {
					out.write(buffer, offset, length);
				} catch (IOException e) {
					hasErrors = true;
				}
			}

			@Override
			public void close() {
				try {
					out.close();
				} catch (IOException e) {
					hasErrors = true;
				}
			}

			@Override
			public void flush() {
				try {
					out.flush();
				} catch (IOException e) {
					hasErrors = true;
				}
			}
		}
	}

	/**
	 * Entry 最终类
	 * 
	 * @author mayu
	 * 
	 */
	private final class Entry {
		private final String key;

		/** Lengths of this entry's files. */
		private final long[] lengths;// 每一个cache文件的长度

		/** True if this entry has ever been published */
		private boolean readable;

		/** The ongoing edit or null if this entry is not being edited. */
		private Editor currentEditor;

		/**
		 * The sequence number of the most recently committed edit to this
		 * entry.
		 */
		private long sequenceNumber;

		private Entry(String key) {
			this.key = key;
			this.lengths = new long[valueCount];
		}

		public String getLengths() throws IOException {
			StringBuilder result = new StringBuilder();
			for (long size : lengths) {
				result.append(' ').append(size);
			}
			return result.toString();
		}

		/**
		 * Set lengths using decimal numbers like "10123".
		 * 设置每一个cache文件的长度（即lengths[i]的长度）
		 */
		private void setLengths(String[] strings) throws IOException {
			if (strings.length != valueCount) {
				throw invalidLengths(strings);
			}

			try {
				for (int i = 0; i < strings.length; i++) {
					lengths[i] = Long.parseLong(strings[i]);
				}
			} catch (NumberFormatException e) {
				throw invalidLengths(strings);
			}
		}

		private IOException invalidLengths(String[] strings) throws IOException {
			throw new IOException("unexpected journal line: "
					+ Arrays.toString(strings));
		}

		public File getCleanFile(int i) {
			return new File(directory, key + "." + i);
		}

		public File getDirtyFile(int i) {
			return new File(directory, key + "." + i + ".tmp");
		}
	}
}
