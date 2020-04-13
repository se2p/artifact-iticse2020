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
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.Key;
import de.uni_passau.fim.se2.litterbox.ast.model.Message;
import de.uni_passau.fim.se2.litterbox.ast.model.event.BackdropSwitchTo;
import de.uni_passau.fim.se2.litterbox.ast.model.event.Clicked;
import de.uni_passau.fim.se2.litterbox.ast.model.event.Event;
import de.uni_passau.fim.se2.litterbox.ast.model.event.GreenFlag;
import de.uni_passau.fim.se2.litterbox.ast.model.event.KeyPressed;
import de.uni_passau.fim.se2.litterbox.ast.model.event.ReceptionOfMessage;
import de.uni_passau.fim.se2.litterbox.ast.model.event.StartedAsClone;
import de.uni_passau.fim.se2.litterbox.ast.model.event.VariableAboveValue;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.StringLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.Identifier;
import de.uni_passau.fim.se2.litterbox.ast.model.variable.StrId;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.EventOpcode;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;
import static de.uni_passau.fim.se2.litterbox.ast.opcodes.EventOpcode.*;

public class EventParser {

    public static final String INPUTS = "WHENGREATERTHANMENU";
    public static final String KEY_OPTION = "KEY_OPTION";
    public static final String BCAST_OPTION = "BROADCAST_OPTION";
    public static final String VARIABLE_MENU = "WHENGREATERTHANMENU";
    public static final String BACKDROP = "BACKDROP";

    public static Event parse(String blockID, JsonNode allBlocks) throws ParsingException {
        Preconditions.checkNotNull(blockID);
        Preconditions.checkNotNull(allBlocks);

        JsonNode current = allBlocks.get(blockID);
        String opcodeString = current.get(OPCODE_KEY).asText();
        Preconditions
                .checkArgument(EventOpcode.contains(opcodeString), "Given blockID does not point to an event block.");

        EventOpcode opcode = EventOpcode.valueOf(opcodeString);
        if (opcode.equals(event_whenflagclicked)) {
            return new GreenFlag();
        } else if (opcode.equals(event_whenkeypressed)) {
            Key key = KeyParser.parse(current, allBlocks);
            return new KeyPressed(key);
        } else if (opcode.equals(event_whenthisspriteclicked) || opcode.equals(event_whenstageclicked)) {
            return new Clicked();
        } else if (opcode.equals(event_whenbroadcastreceived)) {
            JsonNode fields = current.get(FIELDS_KEY);
            String msgValue = fields.get(BCAST_OPTION).get(FIELD_VALUE).asText();
            Message msg = new Message(new StringLiteral(msgValue));
            return new ReceptionOfMessage(msg);
        } else if (opcode.equals(control_start_as_clone)) {
            return new StartedAsClone();
        } else if (opcode.equals(event_whengreaterthan)) {

            String variableValue = current.get(FIELDS_KEY).get(VARIABLE_MENU).get(0).asText();
            Identifier var = new StrId(variableValue);

            NumExpr fieldValue = NumExprParser.parseNumExpr(current, 0, allBlocks);

            return new VariableAboveValue(var, fieldValue);
        } else if (opcode.equals(event_whenbackdropswitchesto)) {
            JsonNode fields = current.get(FIELDS_KEY);
            JsonNode backdropArray = fields.get(BACKDROP);
            String backdropName = backdropArray.get(FIELD_VALUE).asText();
            Identifier id = new StrId(backdropName);
            return new BackdropSwitchTo(id);
        } else {
            throw new IllegalStateException("EventBlock with opcode " + opcode + " was not parsed");
        }
    }
}
