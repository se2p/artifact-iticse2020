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
package de.uni_passau.fim.se2.litterbox.ast.model.expression.bool;

import de.uni_passau.fim.se2.litterbox.ast.model.expression.BinaryExpression;
import de.uni_passau.fim.se2.litterbox.ast.model.touchable.color.Color;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;

public class ColorTouches extends BinaryExpression<Color, Color> implements BoolExpr {

    public ColorTouches(Color operand1, Color operand2) {
        super(operand1, operand2);
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }
}