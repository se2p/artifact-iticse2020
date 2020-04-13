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
package de.uni_passau.fim.se2.litterbox.ast.parser.stmt;

import com.fasterxml.jackson.databind.JsonNode;
import de.uni_passau.fim.se2.litterbox.ast.Constants;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.Stmt;
import de.uni_passau.fim.se2.litterbox.ast.model.statement.UnspecifiedStmt;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.ActorLookStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.ActorSoundStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.BoolExprOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.CallStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.CommonStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.ControlStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.ListStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.NumExprOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.PenOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.SetStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.SpriteLookStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.SpriteMotionStmtOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.StringExprOpcode;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.TerminationStmtOpcode;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class StmtParser {

    public static Stmt parse(String blockID, JsonNode blocks) throws ParsingException {
        Preconditions.checkNotNull(blockID);
        Preconditions.checkNotNull(blocks);
        Preconditions.checkState(blocks.has(blockID), "No block for id %s", blockID);

        JsonNode current = blocks.get(blockID);
        final String opcode = current.get(Constants.OPCODE_KEY).asText();

        if (TerminationStmtOpcode.contains(opcode)) {
            if (!(current.get(Constants.FIELDS_KEY).has("STOP_OPTION")
                    && (current.get(Constants.FIELDS_KEY).get("STOP_OPTION").get(Constants.FIELD_VALUE).asText()
                    .equals("other scripts in sprite")
                    || current.get(Constants.FIELDS_KEY).get("STOP_OPTION").get(Constants.FIELD_VALUE).asText()
                    .equals("other scripts in stage")))) {
                return TerminationStmtParser.parseTerminationStmt(current, blocks);
            }
        }

        if (ActorLookStmtOpcode.contains(opcode)) {
            return ActorLookStmtParser.parse(current, blocks);
        } else if (ControlStmtOpcode.contains(opcode)) {
            return ControlStmtParser.parse(current, blocks);
        } else if (BoolExprOpcode.contains(opcode) || NumExprOpcode.contains(opcode) || StringExprOpcode
                .contains(opcode)) {
            return ExpressionStmtParser.parse(current, blocks);
        } else if (CommonStmtOpcode.contains(opcode)) {
            return CommonStmtParser.parse(current, blocks);
        } else if (SpriteMotionStmtOpcode.contains(opcode)) {
            return SpriteMotionStmtParser.parse(current, blocks);
        } else if (SpriteLookStmtOpcode.contains(opcode)) {
            return SpriteLookStmtParser.parse(current, blocks);
        } else if (ActorSoundStmtOpcode.contains(opcode)) {
            return ActorSoundStmtParser.parse(current, blocks);
        } else if (CallStmtOpcode.contains(opcode)) {
            return CallStmtParser.parse(current, blocks);
        } else if (ListStmtOpcode.contains(opcode)) {
            return ListStmtParser.parse(current, blocks);
        } else if (SetStmtOpcode.contains(opcode)) {
            return SetStmtParser.parse(current, blocks);
        } else if (PenOpcode.contains(opcode)) {
            return PenStmtParser.parse(current, blocks);
        } else {
            return new UnspecifiedStmt();
        }
    }
}
