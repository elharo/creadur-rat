/*
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 */

package org.apache.rat.walker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.rat.api.Document;
import org.apache.rat.api.RatException;
import org.apache.rat.document.impl.ArchiveEntryDocument;
import org.apache.rat.report.RatReport;

/**
 * Walks various kinds of archives files
 */
public class ArchiveWalker extends Walker {

    /**
     * Constructs a walker.
     * @param file not null
     * @param filter filters input files (optional) null when no filtering should be performed
     * @throws FileNotFoundException in case of I/O errors.
     */
    public ArchiveWalker(final File file, final FilenameFilter filter) throws FileNotFoundException {
        super(file, filter);
    }

    /**
     * Run a report over all files and directories in this GZIPWalker,
     * ignoring any files/directories set to be ignored.
     *
     * @param report the defined RatReport to run on this GZIP walker.
     *
     */
    public void run(final RatReport report) throws RatException {

        try {
            ArchiveInputStream<? extends ArchiveEntry> input;

            /* I am really sad that classes aren't first-class objects in
               Java :'( */
            try {
                input = new TarArchiveInputStream(new GzipCompressorInputStream(Files.newInputStream(getBaseFile().toPath())));
            } catch (IOException e) {
                try {
                    input = new TarArchiveInputStream(new BZip2CompressorInputStream(Files.newInputStream(getBaseFile().toPath())));
                } catch (IOException e2) {
                    input = new ZipArchiveInputStream(Files.newInputStream(getBaseFile().toPath()));
                }
            }

            ArchiveEntry entry = input.getNextEntry();

            while (entry != null) {
                File f = new File(entry.getName());
                byte[] contents = new byte[(int) entry.getSize()];
                int offset = 0;
                int length = contents.length;

                while (offset < entry.getSize()) {
                    int actualRead = input.read(contents, offset, length);
                    length -= actualRead;
                    offset += actualRead;
                }

                if (!entry.isDirectory() && isNotIgnored(f)) {
                    report(report, contents, f);
                }

                entry = input.getNextEntry();
            }

            input.close();
        } catch (IOException e) {
            throw new RatException(e);
        }
    }

    public Iterator<Document> getDocuments() {
        try {
            ArchiveInputStream<? extends ArchiveEntry> input =  new ArchiveStreamFactory().createArchiveInputStream(Files.newInputStream(getBaseFile().toPath()));

            return new Iterator<Document>() {
                ArchiveEntry entry = input.getNextEntry();
                @Override
                public boolean hasNext() {
                    if (entry == null) {
                        setNextEntry();
                    }
                    return entry != null;
                }

                @Override
                public Document next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }

                    try {
                        byte[] contents = new byte[(int) entry.getSize()];
                        int offset = 0;
                        int length = contents.length;

                        while (offset < entry.getSize()) {
                            int actualRead = input.read(contents, offset, length);
                            length -= actualRead;
                            offset += actualRead;
                        }

                        return new ArchiveEntryDocument(new File(entry.getName()), contents);
                    } finally {
                        entry = null;
                    }

                }

                private void setNextEntry()  {
                    try {
                        ArchiveEntry candidate = input.getNextEntry();
                        while (candidate != null) {
                            if (!candidate.isDirectory()) {
                                File f = new File(candidate.getName());
                                if (isNotIgnored(new File(candidate.getName()))) {
                                    entry = candidate;
                                    return;
                                }
                            }
                            candidate = input.getNextEntry();
                        }
                        entry = null;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            ArchiveEntry entry = input.getNextEntry();
            while (entry != null) {
                File f = new File(entry.getName());
                byte[] contents = new byte[(int) entry.getSize()];
                int offset = 0;
                int length = contents.length;

                while (offset < entry.getSize()) {
                    int actualRead = input.read(contents, offset, length);
                    length -= actualRead;
                    offset += actualRead;
                }

                if (!entry.isDirectory() && isNotIgnored(f)) {
                    report(report, contents, f);
                }

                entry = input.getNextEntry();
            }

            input.close();
        } catch (IOException | ArchiveException | RatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Report on the given file.
     *
     * @param report the report to process the file with
     * @param file the file to be reported on
     * @throws RatException
     */
    private void report(final RatReport report, final byte[] contents, final File file) throws RatException {
        Document document = new ArchiveEntryDocument(file, contents);
        report.report(document);
    }
}
