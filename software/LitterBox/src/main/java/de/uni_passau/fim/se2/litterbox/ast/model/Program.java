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
package de.uni_passau.fim.se2.litterbox.ast.model;

import de.uni_passau.fim.se2.litterbox.ast.model.variable.Identifier;
import de.uni_passau.fim.se2.litterbox.ast.parser.symboltable.ProcedureDefinitionNameMapping;
import de.uni_passau.fim.se2.litterbox.ast.parser.symboltable.SymbolTable;
import de.uni_passau.fim.se2.litterbox.ast.visitor.ScratchVisitor;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

public class Program extends AbstractNode {

    private final Identifier ident;
    private final ActorDefinitionList actorDefinitionList;
    private final SymbolTable symbolTable;
    private final ProcedureDefinitionNameMapping procedureMapping;

    public Program(Identifier ident, ActorDefinitionList actorDefinitionList, SymbolTable symbolTable,
                   ProcedureDefinitionNameMapping procedureMapping) {
        super(ident, actorDefinitionList);
        this.ident = Preconditions.checkNotNull(ident);
        this.actorDefinitionList = Preconditions.checkNotNull(actorDefinitionList);
        this.procedureMapping = procedureMapping;
        this.symbolTable = symbolTable;
    }

    public Identifier getIdent() {
        return ident;
    }

    public ActorDefinitionList getActorDefinitionList() {
        return actorDefinitionList;
    }

    @Override
    public void accept(ScratchVisitor visitor) {
        visitor.visit(this);
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public ProcedureDefinitionNameMapping getProcedureMapping() {
        return procedureMapping;
    }
}