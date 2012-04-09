package org.moreunit.core.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;
import org.moreunit.core.MoreUnitCore;

public class GenericPropertyPage extends PropertyPage
{
    private final String languageId;
    private final Preferences wsPreferences;
    private LanguagePreferencesWriter prefs;
    private Button projectSpecificSettingsCheckbox;
    private GenericConfigurationPage delegate;

    public GenericPropertyPage(String languageId)
    {
        this.languageId = languageId;
        wsPreferences = MoreUnitCore.get().getPreferences();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        prefs = wsPreferences.get((IProject) getElement()).writerForLanguage(languageId);

        delegate = new GenericConfigurationPage(this, prefs);

        initializeDialogUnits(parent);

        Composite contentComposite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        contentComposite.setLayout(layout);
        contentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        createCheckboxContent(contentComposite);

        delegate.createContents(parent);

        if(prefs.isActive())
        {
            delegate.validate();
        }
        else
        {
            delegate.setEnabled(false);
        }

        return parent;
    }

    private void createCheckboxContent(Composite parent)
    {
        projectSpecificSettingsCheckbox = new Button(parent, SWT.CHECK);
        projectSpecificSettingsCheckbox.setText("Use project specific settings");

        projectSpecificSettingsCheckbox.addSelectionListener(new SelectionListener()
        {
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }

            public void widgetSelected(SelectionEvent e)
            {
                boolean checked = projectSpecificSettingsCheckbox.getSelection();
                prefs.setActive(checked);
                delegate.setEnabled(checked);

                if(checked)
                {
                    delegate.validate();
                }
            }
        });

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        projectSpecificSettingsCheckbox.setLayoutData(gridData);

        projectSpecificSettingsCheckbox.setSelection(prefs.isActive());
    }

    @Override
    public boolean performOk()
    {
        if(! delegate.performOk())
        {
            return false;
        }
        return super.performOk();
    }

    @Override
    protected void performDefaults()
    {
        super.performDefaults();
        delegate.performDefaults();
    }

    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if(! visible)
        {
            return;
        }
    }
}
