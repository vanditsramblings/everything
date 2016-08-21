package com.rambler.tasklipse.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

public abstract class AbstractTabbedPropertyPage extends TabbedPropertySheetPage{

	public AbstractTabbedPropertyPage(ITabbedPropertySheetPageContributor tabbedPropertySheetPageContributor) {
		super(tabbedPropertySheetPageContributor);
	}
	
	@Override
	public void createControl(Composite parent){
		String [] items={"a","b"};
		/*
	     * First, create a section in the configuration tab. Use the widget factory for all
	     * elements.
	     */
	    TabbedPropertySheetWidgetFactory factory = this.getWidgetFactory();
	    final Section sectionProperties = factory.createSection(parent, Section.TITLE_BAR | Section.EXPANDED);
	    sectionProperties.setText("ABC");
	    // Create composite in section
	    final Composite configurationComposite = factory.createFlatFormComposite(sectionProperties);
	    configurationComposite.setLayout(new GridLayout(2, false));

	    // Create content of the section
	    new Label(configurationComposite, SWT.NONE).setText("Label");

	    CCombo algorithmCombo = factory.createCCombo(configurationComposite, SWT.READ_ONLY);
	    // The setData method is used to automatically synchronize the GUI elements with a
	    // configuration value.
	    algorithmCombo.setItems(items);

	    Button saveToFileCheckbox = factory.createButton(configurationComposite, "Button", SWT.CHECK);
	    sectionProperties.setClient(configurationComposite);
	}

}
