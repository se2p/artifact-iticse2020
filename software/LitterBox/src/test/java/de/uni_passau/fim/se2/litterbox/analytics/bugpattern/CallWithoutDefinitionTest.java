/*
 * Copyright (C) 2019 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package de.uni_passau.fim.se2.litterbox.analytics.bugpattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.uni_passau.fim.se2.litterbox.analytics.IssueReport;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class CallWithoutDefinitionTest {
    private static Program empty;
    private static Program callWithoutDef;
    private static Program sportPong;
    private static Program writeTheDraw;
    private static Program scratchHomeVideo;
    private static Program derpyAnimal;
    private static Program malformatedProc;
    private static ObjectMapper mapper = new ObjectMapper();

    @BeforeAll
    public static void setUp() throws IOException, ParsingException {

        File f = new File("./src/test/fixtures/emptyProject.json");
        empty = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/callWithoutDefinition.json");
        callWithoutDef = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/sportpong.json");
        sportPong = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/writeTheDraw.json");
        writeTheDraw = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/scratchHomeVideo.json");
        scratchHomeVideo = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/derpyAnimal.json");
        derpyAnimal = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
        f = new File("./src/test/fixtures/bugpattern/malformatedProc.json");
        malformatedProc = ProgramParser.parseProgram(f.getName(), mapper.readTree(f));
    }

    @Test
    public void testEmptyProgram() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(empty);
        Assertions.assertEquals(0, report.getCount());
    }

    @Test
    public void testCallWithoutDef() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(callWithoutDef);
        Assertions.assertEquals(1, report.getCount());
    }

    @Test
    public void testSportPong() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(sportPong);
        Assertions.assertEquals(0, report.getCount());
    }

    @Test
    public void testWriteTheDraw() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(writeTheDraw);
        Assertions.assertEquals(0, report.getCount());
    }

    @Test
    public void testHomeVideo() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(scratchHomeVideo);
        Assertions.assertEquals(0, report.getCount());
    }

    @Test
    public void testDerpyAnimal() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(derpyAnimal);
        Assertions.assertEquals(0, report.getCount());
    }

    @Test
    public void testMalformatedProc() {
        CallWithoutDefinition parameterName = new CallWithoutDefinition();
        IssueReport report = parameterName.check(malformatedProc);
        System.out.println(report.getPosition());
        Assertions.assertEquals(0, report.getCount());
    }
}
