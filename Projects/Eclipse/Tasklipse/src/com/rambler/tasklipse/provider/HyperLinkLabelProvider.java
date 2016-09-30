package com.rambler.tasklipse.provider;


import java.net.URL;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.rambler.tasklipse.model.Task;

public class HyperLinkLabelProvider extends StyledCellLabelProvider {

	private final CellLabelProvider cellLabelProvider;
	private int columnIndex = -1;
	private int charWidth;

	public HyperLinkLabelProvider(CellLabelProvider cellLabelProvider) {
		this.cellLabelProvider = cellLabelProvider;
	}

	@Override
	public void initialize(ColumnViewer viewer, ViewerColumn column) {
		super.initialize(viewer, column);
		LinkMouseListener mouseListener = new LinkMouseListener(viewer);
		viewer.getControl().addMouseListener(mouseListener);	
	}

	@Override
	protected void paint(Event event, Object element) {
		super.paint(event, element);
		charWidth = event.gc.getFontMetrics().getAverageCharWidth() ;
	}

	@Override
	public void update(ViewerCell cell) {
		cellLabelProvider.update(cell);
		StyleRange s = new StyleRange();
		s.foreground = cell.getItem().getDisplay().getSystemColor(SWT.COLOR_BLUE);
		s.underline = true;
		s.start = 0;
		s.length = cell.getText().length();
		cell.setStyleRanges(new StyleRange[] {s});
		columnIndex = cell.getColumnIndex();
	}

	private final class LinkMouseListener extends MouseAdapter {
		private final ColumnViewer column;
		public LinkMouseListener(ColumnViewer viewer) {column  =  viewer;}
		@Override public void mouseDown(MouseEvent e) {
			Point point = new Point(e.x,e.y);
			ViewerCell cell = column.getCell(point);
			if(cell!=null&&cell.getElement()!=null){
				Task task=(Task) cell.getElement();
				if (cell != null && cell.getColumnIndex() == columnIndex) {
					Rectangle rect = cell.getTextBounds();
					rect.width = cell.getText().length() * charWidth;
					if (rect.contains(point)){
						IPath path = new Path(cell.getText());
						openFile(path,task.getLinenumber());
					}

				}
			}
		}
		private void openFile(IPath path, int lineNumber) {

			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			IWorkbenchPage page=PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			HashMap map = new HashMap();
			map.put(IMarker.LINE_NUMBER, lineNumber);
			//map.put(IWorkbenchPage.EDITOR_ID_ATTR, "org.eclipse.ui.DefaultTextEditor");
			IMarker marker;
			try {
				marker = file.createMarker(IMarker.TEXT);
				marker.setAttributes(map);
				IDE.openEditor(page,marker);
				marker.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
}
