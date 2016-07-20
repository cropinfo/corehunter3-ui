package org.corehunter.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;

public class ShellUtilitiies {

    private Shell shell;
    
    public ShellUtilitiies(Shell shell) {
        super();
        this.shell = shell;
    }

    public void handleError(String dialogTitle, String message) {

        handleError(dialogTitle, message, (String)null) ;
    }
    
    public void handleError(String dialogTitle, String message, String error) {

        if (shell != null) {
            
            IStatus status;

            if (error != null) {
                status = createMultiStatus(message, error);
            } else {
                status = new Status(IStatus.ERROR, "org.corehunter.ui", message) ;
            }

            ErrorDialog.openError(shell, dialogTitle, message, status);
        }
    }
    
    public void handleError(String dialogTitle, String message, Exception e) {

        if (shell != null) {
            
            IStatus status;

            if (e != null) {
                status = createMultiStatus(message, e);
            } else {
                status = new Status(IStatus.ERROR, "org.corehunter.ui", message) ;
            }

            ErrorDialog.openError(shell, dialogTitle, message, status);
        }
    }

    public final Shell getShell() {
        return shell;
    }

    private static MultiStatus createMultiStatus(String message, Throwable t) {

        List<Status> childStatuses = new ArrayList<>();
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

        for (StackTraceElement stackTrace : stackTraces) {
            Status status = new Status(IStatus.ERROR, "org.corehunter.ui", stackTrace.toString());
            childStatuses.add(status);
        }

        MultiStatus ms = new MultiStatus("org.corehunter.ui", IStatus.ERROR,
                childStatuses.toArray(new Status[] {}), message, t);
        return ms;
    }
    
    private static MultiStatus createMultiStatus(String message, String... error) {

        List<Status> childStatuses = new ArrayList<>();

        for (int i = 0 ; i < error.length ; ++i) {
            Status status = new Status(IStatus.ERROR, "org.corehunter.ui", error[i]);
            childStatuses.add(status);
        }

        MultiStatus ms = new MultiStatus("org.corehunter.ui", IStatus.ERROR,
                childStatuses.toArray(new Status[] {}), message, null);
        return ms;
    }
}