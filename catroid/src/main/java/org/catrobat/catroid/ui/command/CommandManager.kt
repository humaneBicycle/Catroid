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

import android.util.Log
import org.catrobat.catroid.ui.command.implementation.Command
import org.catrobat.catroid.ui.command.provider.Provider
import org.catrobat.catroid.ui.recyclerview.fragment.SceneListFragment
import java.util.Stack

class CommandManager {
    var undoList: Stack<Command>
    var redoList: Stack<Command>
    var undoRedoListener: UndoRedoListener? = null

    init {
        undoList = Stack()
        redoList = Stack()
    }

    fun undo(provider: Provider) {
        if (!undoList.isEmpty()) {
            val commandToRedo = undoList.pop()
            commandToRedo.undo(provider)
            redoList.add(commandToRedo)
        }
        notifyUndoRedoButtons()
    }

    fun redo(provider: Provider) {
        if (!redoList.isEmpty()) {
            val command = redoList.pop()
            command.redo(provider)
            undoList.add(command)
        }
        notifyUndoRedoButtons()
    }

    fun addUndoListener(undoListener: UndoRedoListener?) {
        undoRedoListener = undoListener
    }

    fun executeCommand(command: Command, provider: Provider) {
        command.execute(provider)
        undoList.add(command)
        redoList.clear()
        notifyUndoRedoButtons()
    }
    fun executeWithoutUndo(command:Command, provider: Provider){
        command.execute(provider)
    }

    fun notifyUndoRedoButtons(){
        if (undoRedoListener != null) {
            if (redoList.isEmpty()) {
                undoRedoListener!!.isRedoAvailable(false)
            }else{
                undoRedoListener!!.isRedoAvailable(true)
            }
            if (undoList.isEmpty()) {
                undoRedoListener!!.isUndoAvailable(false)
            }else{
                undoRedoListener!!.isUndoAvailable(true)
            }
        }

        Log.d(SceneListFragment.TAG, "undo stack: "+undoList.size+" redo stack:" +redoList.size )

    }

    fun setUndoStack(commands:Stack<Command>){
        this.undoList=commands
    }
    fun getRedoStack():Stack<Command>{
        return undoList
    }
    interface onCommandCompleteCallback{
        fun onComplete()
        fun onSuccess()
        fun onFailure()
    }
}