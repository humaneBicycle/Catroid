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

import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.io.XstreamSerializer;
import org.catrobat.catroid.ui.recyclerview.controller.SceneController;
import org.catrobat.catroid.ui.recyclerview.fragment.SceneListFragment;
import org.catrobat.catroid.utils.ToastUtil;

import static org.catrobat.catroid.io.asynctask.ProjectLoaderKt.loadProject;

public class RenameCommand implements Command {

	SceneListFragment sceneListFragment;
	SceneController sceneController;
	Scene item;
	String name;
	ProjectManager projectManager;
	String oldName;

	public RenameCommand(SceneListFragment sceneListFragment, SceneController sceneController,
			Scene item, String name, ProjectManager projectManager){
		this.sceneController=sceneController;
		this.sceneListFragment=sceneListFragment;
		this.item=item;
		this.name=name;
		this.projectManager=projectManager;
		this.oldName=item.getName();

	}

	public void renameItem(){
		Log.d("abh", "oldname: "+oldName+" newName: "+name+" sceneToRename:"+item.getName());

		if (!item.getName().equals(name)) {
			if (sceneController.rename(item, name)) {
				Project currentProject = projectManager.getCurrentProject();
				XstreamSerializer.getInstance().saveProject(currentProject);
				loadProject(currentProject.getDirectory(),
						sceneListFragment.getActivity().getApplicationContext());
				sceneListFragment.initializeAdapter();
			} else {
				ToastUtil.showError(sceneListFragment.getActivity(), R.string.error_rename_scene);
			}
		}
		sceneListFragment.finishActionMode();
	}
	public void renameItem(Scene item, String name){

		if (!item.getName().equals(name)) {
			Scene sceneToRename =
					projectManager.getCurrentProject().getSceneWithName(item.getName());
			if (sceneController.rename(sceneToRename, name)) {
				Project currentProject = projectManager.getCurrentProject();
				XstreamSerializer.getInstance().saveProject(currentProject);
				loadProject(currentProject.getDirectory(),
						sceneListFragment.getActivity().getApplicationContext());
				sceneListFragment.initializeAdapter();
			} else {
				ToastUtil.showError(sceneListFragment.getActivity(), R.string.error_rename_scene);
			}
		}

	}

	@Override
	public void unExecute() {
		renameItem(item,oldName);
	}

	public RenameCommand getCommand(){
		return this;
	}
}