package com.rambler.tasklipse.views;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

import com.rambler.tasklipse.model.Task;
import com.rambler.tasklipse.model.TaskColumn;
import com.rambler.tasklipse.provider.TasklipseContentProvider;
import com.rambler.tasklipse.provider.TasklipseLabelProvider;


public class TasklipseView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.rambler.tasklipse.views.TasklipseView";

	TableViewer viewer=null;
	TasklipseContentProvider contentProvider;
	TasklipseLabelProvider labelProvider;
	TabFolder tabFolder;
	
	/**
	 * The constructor.
	 */
	public TasklipseView() {
	}

	public void initView(Composite parent){
		contentProvider=new TasklipseContentProvider();
		labelProvider=new TasklipseLabelProvider();
		//tabFolder = new TabFolder(parent, SWT.BORDER);
	}
	
	public void initTable(Composite parent){
		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setUseHashlookup(true);
		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);

		//make lines and header visible
		//-----------------------------------------------------
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true); 
		//-----------------------------------------------------
		
		
		//adding selection change listener
		//-----------------------------------------------------
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			  @Override
			  public void selectionChanged(SelectionChangedEvent event) {
			    IStructuredSelection selection = viewer.getStructuredSelection();
			  }
			});
		//-----------------------------------------------------
		
		
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
	    
	    //creating columns
	    //-----------------------------------------------------
	    createColumns(parent,viewer);
	    //-----------------------------------------------------
	    
	}

	@Override  
	public void createPartControl(Composite parent) {  

		//InitializingView
		initView(parent);
		
		//Initializing table
		initTable(parent);

		/*for (int loopIndex = 0; loopIndex < 1; loopIndex++) {
			TabItem tabItem = new TabItem(tabFolder, SWT.NULL);
			tabItem.setText("Tab " + loopIndex);

			Table table=null;
			try {
				viewer=createTable(loopIndex,tabFolder);
			} catch (CoreException e) {

				e.printStackTrace();
			}
			tabItem.setControl(table);
		}
		tabFolder.setSize(400, 200);*/
	}  

	private void createTable(Composite parent) throws CoreException {
		/*Table table = new Table(parent, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		table.setHeaderVisible(true);
		String[] titles = { "Type", "Resource", "Text", "Line Number" , "Created Time" };

		for (int index = 0; index < titles.length; index++) {
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[index]);
		}

		for (IMarker marker : TasklipseUtils.getTaskListForAllProjects()) {
			TableItem item = new TableItem(table, SWT.NULL);
			item.setText(0,TasklipseUtils.getTaskType(marker));
			item.setText(1,marker.getResource().getLocation().toOSString());
			item.setText(2,marker.getAttribute(IMarker.MESSAGE)!=null?marker.getAttribute(IMarker.MESSAGE).toString():"NA");
			item.setText(3,marker.getAttribute(IMarker.LINE_NUMBER).toString());
			Date date = new Date(marker.getCreationTime());
			Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
			item.setText(4,format.format(date));
		}

		for (int index = 0; index < titles.length; index++) {
			table.getColumn(index).pack();
		}

		table.setBounds(25, 25, 220, 200);

		table.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					System.out.println("You checked " + event.item);
				} else {
					System.out.println("You selected " + event.item);
				}
			}
		});
		return table;*/

	


	}
	
	private void createColumns(final Composite parent, final TableViewer viewer) {
		
		for(TaskColumn taskCol:TaskColumn.values()){
			TableViewerColumn viewCol = createTableViewerColumn(taskCol.getDisplayName(), taskCol.getBounds(),taskCol.getColIndex());
			viewCol.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        Task task = (Task) element;
		        return task.getTaskName();
		      }
		    });
		}
	    
	  }
	
	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	        SWT.NONE);
	    final TableColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(bound);
	    column.setResizable(true);
	    column.setMoveable(true);
	    return viewerColumn;
	  }



	private void showMessage(String message) {
		MessageDialog.openInformation(
				getSite().getShell(),
				"Tasklipse",
				message);
	}


	@Override
	public void setFocus() {

	}


	public String getContributorId() {
		return ID;
	}

	public Object getAdapter(Class adapter) { 
		return super.getAdapter(adapter);
	}



}
