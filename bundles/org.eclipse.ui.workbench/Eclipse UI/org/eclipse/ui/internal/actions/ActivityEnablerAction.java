/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.internal.actions;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.activities.IMutableActivityManager;

import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.activities.ui.ActivityEnabler;

/**
 * Activates the Activity configuration dialog. 
 * 
 * @since 3.0
 */
public class ActivityEnablerAction extends Action implements ActionFactory.IWorkbenchAction {
	private IWorkbenchWindow workbenchWindow;
    private ActivityEnabler enabler;
	private IMutableActivityManager activityManager;

    /**
     * Create a new instance of the receiver.
     * 
     * @since 3.0
     */
    public ActivityEnablerAction(IWorkbenchWindow window) {
        super(WorkbenchMessages.getString("ActivityEnablementAction.text")); //$NON-NLS-1$
        if (window == null) {
        	throw new IllegalArgumentException();
        }
        this.workbenchWindow = window;
        // TODO: down-cast
        this.activityManager = (IMutableActivityManager) window.getWorkbench().getActivityManager();
    }

    /*
     * (non-Javadoc) @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
    	if (workbenchWindow == null) {
    		// action has been disposed
    		return;
    	}
        Dialog d = new Dialog(workbenchWindow.getShell()) {

            /*
             * (non-Javadoc) @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
             */
            protected Control createDialogArea(Composite parent) {
                Composite composite = (Composite) super.createDialogArea(parent);
                GridData data = new GridData(GridData.FILL_BOTH);
                data.widthHint = 600;
                data.heightHint = 240;

                enabler = new ActivityEnabler(activityManager);
                enabler.createControl(composite).setLayoutData(data);

                return composite;
            }

            /*
             * (non-Javadoc) @see org.eclipse.jface.dialogs.Dialog#okPressed()
             */
            protected void okPressed() {
                if (enabler != null) {
                    enabler.updateActivityStates();
                }
                super.okPressed();
            }
        };
        d.open();
    }

	/* (non-Javadoc)
	 * @see org.eclipse.ui.actions.ActionFactory.IWorkbenchAction#dispose()
	 */
	public void dispose() {
		workbenchWindow = null;
	}
}
