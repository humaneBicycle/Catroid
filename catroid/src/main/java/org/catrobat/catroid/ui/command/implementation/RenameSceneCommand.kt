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
package org.catrobat.catroid.ui.command.implementation

import org.catrobat.catroid.ProjectManager
import org.catrobat.catroid.R
import org.catrobat.catroid.io.XstreamSerializer
import org.catrobat.catroid.io.asynctask.loadProject
import org.catrobat.catroid.ui.command.provider.Provider
import org.catrobat.catroid.ui.command.provider.SceneSceneCommandProvider
import org.catrobat.catroid.ui.recyclerview.controller.SceneController
import org.catrobat.catroid.ui.recyclerview.fragment.SceneListFragment
import org.catrobat.catroid.utils.ToastUtil

class RenameSceneCommand(
    var previousName: String, var name: String
) : Command {

    override fun execute(provider: Provider) {
        val sceneListFragment = (provider as SceneSceneCommandProvider).setSceneListFragment()
        val sceneController= provider.setSceneController()
        val projectManager= provider.setProjectListFragment()
        renameItem(previousName, name,sceneListFragment,sceneController,projectManager)
    }

    override fun undo(provider: Provider) {
        val sceneListFragment = (provider as SceneSceneCommandProvider).setSceneListFragment()
        val sceneController= provider.setSceneController()
        val projectManager= provider.setProjectListFragment()
        renameItem(name, previousName,sceneListFragment,sceneController,projectManager)
    }

    override fun redo(provider: Provider){
        val sceneListFragment = (provider as SceneSceneCommandProvider).setSceneListFragment()
        val sceneController= provider.setSceneController()
        val projectManager= provider.setProjectListFragment()
        renameItem(previousName, name,sceneListFragment,sceneController,projectManager)
    }

    val command: RenameSceneCommand
        get() = this


    fun renameItem(oldName: String, newName: String,  sceneListFragment: SceneListFragment,
        sceneController: SceneController,projectManager:ProjectManager) {
        if (oldName != newName) {
            val sceneToRename = projectManager.currentProject.getSceneWithName(oldName)
            if (sceneController.rename(sceneToRename, newName)) {
                val currentProject = projectManager.currentProject
                XstreamSerializer.getInstance().saveProject(currentProject)
                loadProject(
                    currentProject.directory,
                    sceneListFragment.requireActivity().applicationContext
                )
                sceneListFragment.initializeAdapter()
            } else {
                ToastUtil.showError(sceneListFragment.activity, R.string.error_rename_scene)
            }
        }
    }
}