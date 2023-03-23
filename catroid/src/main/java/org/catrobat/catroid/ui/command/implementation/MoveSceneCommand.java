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

package org.catrobat.catroid.ui.command.implementation;

import org.catrobat.catroid.ui.command.callback.Callback;
import org.catrobat.catroid.ui.command.callback.SceneListCommandCallback;
import org.catrobat.catroid.ui.recyclerview.adapter.SceneAdapter;

public class MoveSceneCommand implements Command {

	int sourcePosition, targetPosition;

	public MoveSceneCommand(int sourcePosition, int targetPosition){
		this.sourcePosition=sourcePosition;
		this.targetPosition=targetPosition;

	}
	@Override
	public void execute(Callback mediator) {

	}
	@Override
	public void redo(Callback mediator) {
		SceneAdapter adapter = ((SceneListCommandCallback)mediator).setSceneAdapter();
		adapter.onItemMove(sourcePosition,targetPosition);
	}

	@Override
	public void undo(Callback mediator) {
		SceneAdapter adapter = ((SceneListCommandCallback)mediator).setSceneAdapter();
		adapter.onItemMove(targetPosition,sourcePosition);
	}
}
