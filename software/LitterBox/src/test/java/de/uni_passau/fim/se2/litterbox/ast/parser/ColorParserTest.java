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
package de.uni_passau.fim.se2.litterbox.ast.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.truth.Truth;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.ActorDefinition;
import de.uni_passau.fim.se2.litterbox.ast.model.ActorDefinitionList;
import de.uni_passau.fim.se2.litterbox.ast.model.Program;
import de.uni_passau.fim.se2.litterbox.ast.model.Script;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.ColorTouches;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.bool.Touching;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.ColorLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.ExpressionStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.color.FromNumber;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.TestCase.fail;

class ColorParserTest {

    private static JsonNode project;

    @BeforeAll
    public static void setup() {
        String path = "src/test/fixtures/colorBlocks.json";
        File file = new File(path);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            project = objectMapper.readTree(file);
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testFromNumberInColorTouches() {
        try {
            Program program = ProgramParser.parseProgram("Test", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            Truth.assertThat(list.getDefintions().size()).isEqualTo(2);

            final ActorDefinition first = list.getDefintions().get(1);
            final Script script = first.getScripts().getScriptList().get(0);
            final ExpressionStmt expressionStmt = (ExpressionStmt) script.getStmtList().getStmts().getListOfStmt()
                    .get(0);
            Truth.assertThat(expressionStmt.getExpression()).isInstanceOf(ColorTouches.class);
            ColorTouches expression = (ColorTouches) expressionStmt.getExpression();

            Truth.assertThat(expression.getOperand1()).isInstanceOf(FromNumber.class);
            Truth.assertThat(expression.getOperand2()).isInstanceOf(FromNumber.class);
        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testColorLiteral() {
        try {
            Program program = ProgramParser.parseProgram("Test", project);
            ActorDefinitionList list = program.getActorDefinitionList();
            Truth.assertThat(list.getDefintions().size()).isEqualTo(2);

            final ActorDefinition first = list.getDefintions().get(1);
            final Script script = first.getScripts().getScriptList().get(1);
            final ExpressionStmt expressionStmt = (ExpressionStmt) script.getStmtList().getStmts().getListOfStmt()
                    .get(0);
            Truth.assertThat(expressionStmt.getExpression()).isInstanceOf(Touching.class);
            Touching expression = (Touching) expressionStmt.getExpression();

            Truth.assertThat(expression.getTouchable()).isInstanceOf(ColorLiteral.class);
        } catch (ParsingException e) {
            e.printStackTrace();
            fail();
        }
    }
}