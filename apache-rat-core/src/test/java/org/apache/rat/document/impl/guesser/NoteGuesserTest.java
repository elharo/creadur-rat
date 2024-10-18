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
package org.apache.rat.document.impl.guesser;

import org.apache.rat.document.impl.DocumentName;
import org.apache.rat.testhelpers.TestingDocument;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class NoteGuesserTest {

    private DocumentName mkNameSet(String name) {
        return new DocumentName(name, "", "/", true);
    }
    @Test
    public void testMatches() {
        assertTrue(NoteGuesser.isNote(new TestingDocument("DEPENDENCIES")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("LICENSE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("LICENSE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("NOTICE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("NOTICE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("README")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("README.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/DEPENDENCIES")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/LICENSE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/LICENSE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/NOTICE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/NOTICE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/README")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src/test/README.txt")));

    }

    @Disabled("RAT-390: old tests that do not work anymore")
    public void isNoteWithPathWindowsLike() {
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\DEPENDENCIES")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\LICENSE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\LICENSE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\NOTICE")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\NOTICE.txt")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\README")));
        assertTrue(NoteGuesser.isNote(new TestingDocument("src\\test\\README.txt")));
    }
}
