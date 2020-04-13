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
import de.uni_passau.fim.se2.litterbox.ast.model.expression.Expression;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.StringLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.AsTouchable;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.Edge;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.MousePointer;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.SpriteTouchable;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.Touchable;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.BoolExprOpcode;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;

public class TouchableParser {

    public static final String TOUCHINGOBJECTMENU = "TOUCHINGOBJECTMENU";
    public static final String TOUCHING_MOUSE = "_mouse_";
    public static final String TOUCHING_EDGE = "_edge_";

    public static Touchable parseTouchable(JsonNode current, JsonNode allBlocks) throws ParsingException {
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(allBlocks);
        final String opcodeString = current.get(OPCODE_KEY).asText();

        if (BoolExprOpcode.sensing_touchingobject.name().equals(opcodeString)) {
            List<JsonNode> inputsList = new ArrayList<>();
            current.get(Constants.INPUTS_KEY).elements().forEachRemaining(inputsList::add);
            if (getShadowIndicator((ArrayNode) inputsList.get(0)) == 1) {
                return getTouchableMenuOption(current, allBlocks);
            } else {
                Expression expr = ExpressionParser.parseExpression(current, TOUCHINGOBJECTMENU, allBlocks);
                return new AsTouchable(expr);
            }
        } else if (BoolExprOpcode.sensing_touchingcolor.name().equals(opcodeString)) {
            return ColorParser.parseColor(current, 0, allBlocks);
        } else {
            throw new RuntimeException("Not implemented yet");
        }
    }

    private static Touchable getTouchableMenuOption(JsonNode current, JsonNode allBlocks) throws ParsingException {
        String menuID = current.get(INPUTS_KEY).get(TOUCHINGOBJECTMENU).get(POS_INPUT_VALUE).asText();
        String touchingObject = allBlocks.get(menuID).get(FIELDS_KEY).get(TOUCHINGOBJECTMENU).get(0).asText();

        if (touchingObject.equals(TOUCHING_MOUSE)) {
            return new MousePointer();
        } else if (touchingObject.equals(TOUCHING_EDGE)) {
            return new Edge();
        } else {
            return new SpriteTouchable(new StringLiteral(touchingObject));
        }
    }

    static int getShadowIndicator(ArrayNode exprArray) {
        return exprArray.get(Constants.POS_INPUT_SHADOW).asInt();
    }
}
