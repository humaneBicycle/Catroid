/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2023 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.ui.command;

import java.util.Stack;

public class CommandManager {
	public Stack<Command> undoList;
	public Stack<Command> redoList;
	UndoRedoListener undoRedoListener;

	public CommandManager(){
		undoList = new Stack<>();
		redoList = new Stack<>();
	}

	public void undo(){
		if(undoList.size()>0) {
			Command commandToRedo = undoList.peek();
			commandToRedo.unExecute();
			redoList.add(commandToRedo);
			undoList.pop();
		}
		if(undoRedoListener!=null){
			if(undoList.size()==0){
				undoRedoListener.isUndoAvailable(false);
			}
			undoRedoListener.isRedoAvailable(true);
		}

	}

	public void redo(){
		if(redoList.size()>0){
			Command command = redoList.peek();
			command.execute();
			undoList.add(command);
			redoList.pop();
		}
		if(undoRedoListener!=null){
			if(redoList.size()==0){
				undoRedoListener.isRedoAvailable(false);
			}
			undoRedoListener.isUndoAvailable(true);
		}
	}

	public void addUndoListener(UndoRedoListener undoListener){
		this.undoRedoListener=undoListener;
	}
	public void executeCommand(Command command){
		command.execute();
		undoList.add(command);
		redoList.clear();
		if(undoRedoListener!=null){
			undoRedoListener.isUndoAvailable(true);
		}
	}
}