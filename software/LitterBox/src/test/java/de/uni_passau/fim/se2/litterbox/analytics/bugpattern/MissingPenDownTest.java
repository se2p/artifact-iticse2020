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
import com.google.common.truth.Truth;
import de.uni_passau.fim.se2.litterbox.analytics.IssueReport;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;

class MissingPenDownTest {

    private static Program program;

    @BeforeAll
    public static void setup() {
        String path = "src/test/fixtures/bugpattern/missingPenDown.json";
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            program = ProgramParser.parseProgram("missingPenDown", objectMapper.readTree(file));
        } catch (IOException | ParsingException e) {
            fail();
        }
    }

    @Test
    public void testMissingPenDown() {
        MissingPenDown finder = new MissingPenDown();
        final IssueReport result = finder.check(program);
        Truth.assertThat(result.getCount()).isEqualTo(1);
        Truth.assertThat(result.getPosition().get(0)).isEqualTo("Apple");
        System.out.println(result);
    }
}