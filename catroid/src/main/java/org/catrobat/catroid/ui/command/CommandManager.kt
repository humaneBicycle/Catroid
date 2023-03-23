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
package org.catrobat.catroid.ui.command

import org.catrobat.catroid.ui.command.implementation.Command
import org.catrobat.catroid.ui.command.callback.Callback
import java.util.Stack

class CommandManager {
    var undoList: Stack<Command>
    var redoList: Stack<Command>
    var undoRedoListener: UndoRedoListener? = null

    init {
        undoList = Stack()
        redoList = Stack()
    }

    fun undo(callback: Callback) {
        if (!undoList.isEmpty()) {
            val commandToRedo = undoList.peek()
            commandToRedo.undo(callback)
            redoList.add(commandToRedo)
            undoList.pop()
        }
        notifyUndoRedoButtons()
    }

    fun redo(callback: Callback) {
        if (!redoList.isEmpty()) {
            val command = redoList.peek()
            command.redo(callback)
            undoList.add(command)
            redoList.pop()
        }
        notifyUndoRedoButtons()
    }

    fun addUndoListener(undoListener: UndoRedoListener?) {
        undoRedoListener = undoListener
    }

    fun executeCommand(command: Command, callback: Callback) {
        command.execute(callback)
        undoList.add(command)
        redoList.clear()
        if (undoRedoListener != null) {
            undoRedoListener!!.isUndoAvailable(true)
        }
    }
    fun executeWithoutUndo(command:Command, callback: Callback){
        command.execute(callback)
    }

    fun notifyUndoRedoButtons(){
        if (undoRedoListener != null) {
            if (redoList.isEmpty()) {
                undoRedoListener!!.isRedoAvailable(false)
            }
            undoRedoListener!!.isUndoAvailable(true)
        }
        if (undoRedoListener != null) {
            if (undoList.isEmpty()) {
                undoRedoListener!!.isUndoAvailable(false)
            }
            undoRedoListener!!.isRedoAvailable(true)
        }
    }

    fun setUndoStack(commands:Stack<Command>){
        this.undoList=commands
    }
    fun getRedoStack():Stack<Command>{
        return undoList
    }
}