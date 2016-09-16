package com.rambler.tasklipse.views;


import java.util.ArrayList;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.rambler.tasklipse.model.Task;
import com.rambler.tasklipse.model.TaskColumn;
import com.rambler.tasklipse.provider.TasklipseLabelProvider;
import com.rambler.tasklipse.provider.HyperLinkLabelProvider;
import com.rambler.tasklipse.util.TasklipseUtils;

/**
 * @author vandit
 * 
 */

public class TasklipseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.rambler.tasklipse.views.TasklipseView";

	Table table=null;
	TableViewer viewer=null;
	TasklipseContentProvider contentProvider=new TasklipseContentProvider();
	TasklipseLabelProvider labelProvider=new TasklipseLabelProvider();
	IResourceChangeListener resourceChangedListener=null;


	public static boolean TASKLIPSE_PROP_RESOURCE_CHANGE_LISTENER=true;

	ArrayList<Task> tasks=new ArrayList<Task>();
	/**
	 * The constructor.
	 */
	public TasklipseView() {
	}

	public void initView(Composite parent){
		contentProvider=new TasklipseContentProvider();
		labelProvider=new TasklipseLabelProvider();
	}

	public void initTable(Composite parent) throws CoreException{

		createTableViewer(parent);

		createTable();

		createColumns();

		viewer.setContentProvider(new TasklipseContentProvider());
		try {
			tasks=TasklipseUtils.getTaskListForAllProjects();

			viewer.setInput(tasks);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	private void createTableViewer(Composite parent) {

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setUseHashlookup(true);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);

		//layout of the view
		//-----------------------------------------------------
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		//-----------------------------------------------------

		viewer.setColumnProperties(getColumnNames());

	}

	private String[] getColumnNames() {
		TaskColumn[] columns = TaskColumn.values();
		String[] names = new String[columns.length];

		for (int i = 0; i < columns.length; i++) {
			names[i] = columns[i].getDisplayName();
		}

		return names;

	}

	@Override  
	public void createPartControl(Composite parent) {  

		//InitializingView
		initView(parent);

		//Initializing table
		try {
			initTable(parent);
			if(TASKLIPSE_PROP_RESOURCE_CHANGE_LISTENER)
				addResourceChangeListener();
		} catch (CoreException e) {
			System.out.println("Error occurred while initializing table");
			e.printStackTrace();
		}
	}  


	private void addResourceChangeListener() {

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		resourceChangedListener = new IResourceChangeListener() {

			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				try {
					if (event.getType() != IResourceChangeEvent.POST_CHANGE)
						return;
					tasks=TasklipseUtils.getTaskListForAllProjects();
				} catch (CoreException e) {
					e.printStackTrace();
				}
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						viewer.setInput(tasks);
						viewer.refresh();
					}
				});
			}
		};
		workspace.addResourceChangeListener(resourceChangedListener);

	}

	private void createTable(){
		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
	}


	private void createColumns() {

		for(TaskColumn taskCol:TaskColumn.values()){

			if(taskCol.equals(TaskColumn.PRIORITY)){
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
				final TableColumn column = col.getColumn();
				column.setText(taskCol.getDisplayName());
				column.setWidth(taskCol.getBounds());
				column.setResizable(true);
				column.setMoveable(true);
				col.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Task)element).getPriority()+"";
					}

					@Override
					public Color getBackground(final Object element) {
						if (element instanceof Task) {
							int priority=((Task) element).getPriority();
							if (priority==0) {
								return super.getBackground(element);
							}
							else if(priority==1){
								return new Color(Display.getDefault(), 0x72, 0xF0, 0x8D);	
							}
							else if(priority==2){
								return new Color(Display.getDefault(), 0xAF, 0xFD, 0xC0);	
							}
							else if(priority==3){
								return new Color(Display.getDefault(), 0xF6, 0xF6, 0x9A);	
							}
							else if(priority==4){
								return new Color(Display.getDefault(), 0xEA, 0xA1, 0x32);	
							}
							else if(priority>=5){
								return new Color(Display.getDefault(), 0xEC, 0x7A, 0x54);	
							}
						}
						return super.getBackground(element);
					}
				});
			}
			else if(taskCol.equals(TaskColumn.MESSAGE)){
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
				final TableColumn column = col.getColumn();
				column.setText(taskCol.getDisplayName());
				column.setWidth(taskCol.getBounds());
				column.setResizable(true);
				column.setMoveable(true);
				col.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Task)element).getMessage();
					}
				});
			}
			else if(taskCol.equals(TaskColumn.RESOURCE)){
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
				final TableColumn column = col.getColumn();
				column.setText(taskCol.getDisplayName());
				column.setWidth(taskCol.getBounds());
				column.setResizable(true);
				column.setMoveable(true);

				ColumnLabelProvider clp=new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Task)element).getTaskResource();
					}
				};

				col.setLabelProvider(new HyperLinkLabelProvider(clp));
			}
			else if(taskCol.equals(TaskColumn.TYPE)){
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
				final TableColumn column = col.getColumn();
				column.setText(taskCol.getDisplayName());
				column.setWidth(taskCol.getBounds());
				column.setResizable(true);
				column.setMoveable(true);
				col.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Task)element).getTaskType();
					}
				});
			}
			else if(taskCol.equals(TaskColumn.CREATEDTIME)){
				TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
				final TableColumn column = col.getColumn();
				column.setText(taskCol.getDisplayName());
				column.setWidth(taskCol.getBounds());
				column.setResizable(true);
				column.setMoveable(true);
				col.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						return ((Task)element).getCreatedTime();
					}
				});
			}
		}

	}

	private TableViewerColumn createTableColumn(String title, int bound, final int colNumber) {
		TableViewerColumn col = new TableViewerColumn(viewer, SWT.LEFT);
		final TableColumn column = col.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return col;
	}



	private void showMessage(String message) {
		MessageDialog.openInformation(
				getSite().getShell(),
				"Tasklipse",
				message);
	}


	public void setFocus() {
		viewer.getControl().setFocus();
	}


	public String getContributorId() {
		return ID;
	}

	public Object getAdapter(Class adapter) { 
		return super.getAdapter(adapter);
	}

	@Override
	public void dispose(){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(resourceChangedListener);
		viewer=null;
	}

	public class TasklipseContentProvider implements IStructuredContentProvider{

		@Override
		public void dispose() {

		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

		}

		@Override
		public Object[] getElements(Object list) {
			return tasks.toArray();
		}

	}

}
