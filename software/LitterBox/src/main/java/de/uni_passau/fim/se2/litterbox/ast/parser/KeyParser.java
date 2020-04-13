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
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.uni_passau.fim.se2.litterbox.ast.Constants;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Key;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.UnspecifiedNumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.BoolExprOpcode;

import java.util.ArrayList;
import java.util.List;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;

public class KeyParser {

    public static final String KEY_OPTION = "KEY_OPTION";
    public static final int UPARROW = 38;
    public static final int DOWNARROW = 40;
    public static final int RIGHTARROW = 39;
    public static final int LEFTARROW = 37;
    public static final int SPACE = 32;
    public static final int ANYKEY = 0;

    public static Key parse(JsonNode current, JsonNode allBlocks) throws ParsingException {

        JsonNode block;
        final String opcodeString = current.get(OPCODE_KEY).asText();
        if (BoolExprOpcode.sensing_keypressed.name().equals(opcodeString)) {

            List<JsonNode> inputsList = new ArrayList<>();
            current.get(Constants.INPUTS_KEY).elements().forEachRemaining(inputsList::add);
            if (getShadowIndicator((ArrayNode) inputsList.get(0)) == 1) {
                // If there is only the menu in the inputs, we evaluate the menu
                String menuBlockID = current.get(INPUTS_KEY).get(KEY_OPTION).get(POS_INPUT_VALUE).asText();
                block = allBlocks.get(menuBlockID);
            } else {
                // If there is a variable or expression we evaluate it and use it as key;
                final NumExpr numExpr = NumExprParser.parseNumExpr(current, 0, allBlocks);
                return new Key(numExpr);
            }
        } else {
            block = current;
        }
        if (block == null) {
            return new Key(new UnspecifiedNumExpr());
        }
        String keyValue = block.get(FIELDS_KEY).get(KEY_OPTION).get(FIELD_VALUE).asText();
        switch (keyValue) {
        case "space":
            return new Key(new NumberLiteral(SPACE));
        case "up arrow":
            return new Key(new NumberLiteral(UPARROW));
        case "down arrow":
            return new Key(new NumberLiteral(DOWNARROW));
        case "left arrow":
            return new Key(new NumberLiteral(LEFTARROW));
        case "right arrow":
            return new Key(new NumberLiteral(RIGHTARROW));
        case "any":
            return new Key(new NumberLiteral(ANYKEY));
        default:
            if (keyValue.length() > 0) {
                return new Key(new NumberLiteral(keyValue.charAt(0)));
            } else {
                // It is not clear how this can happen, but it happens sometimtes.
                return new Key(new NumberLiteral(0));
            }
        }
    }

    static int getShadowIndicator(ArrayNode exprArray) {
        return exprArray.get(Constants.POS_INPUT_SHADOW).asInt();
    }
}
