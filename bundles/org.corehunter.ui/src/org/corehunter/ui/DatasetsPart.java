/*******************************************************************************
 * Copyright 2016 Guy Davenport
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.corehunter.ui;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import uno.informatics.data.Dataset;
import uno.informatics.data.dataset.DatasetException;
import uno.informatics.data.dataset.FeatureData;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Table;

public class DatasetsPart extends DatasetServiceClient {
    private DatasetTable datasetTable = null;
    private Button btnAddDataset;
    private Spinner spinnerSize;
    private Spinner spinnerIntensity;
    private Button btnStart;
    private Button btnReset;
    private Dataset selectedDataset;
    private int selectedDatasetSize;
    private Button btnRemoveDataset;
    private Label lblDatasetSize;
    private Button btnView;
    private PartUtilitiies partUtilitiies;
    private Table table;
    private Button btnAddObjective;
    private Button btnRemoveObjective;

    @Inject
    public DatasetsPart() {
    }

    @PostConstruct
    public void postConstruct(Composite parent, EPartService partService, EModelService modelService,
            MApplication application) {

        partUtilitiies = new PartUtilitiies(partService, modelService, application);

        parent.setLayout(new GridLayout(1, false));

        Group grpDatasets = new Group(parent, SWT.NONE);
        grpDatasets.setText("Datasets");
        grpDatasets.setLayout(new GridLayout(1, false));
        grpDatasets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

        Composite datasetTableComposite = new Composite(grpDatasets, SWT.NONE);
        datasetTableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        datasetTable = new DatasetTable();

        datasetTable.createPartControl(datasetTableComposite);

        Composite datasetButtonComposite = new Composite(grpDatasets, SWT.NONE);
        datasetButtonComposite.setLayout(new GridLayout(3, false));

        btnAddDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnAddDataset.setText("Add");

        btnAddDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addDataset();
            }
        });

        btnRemoveDataset = new Button(datasetButtonComposite, SWT.NONE);
        btnRemoveDataset.setText("Remove");

        btnRemoveDataset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeDataset();
            }
        });

        btnView = new Button(datasetButtonComposite, SWT.NONE);
        btnView.setText("View");

        btnView.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                viewDataset();
            }
        });

        datasetTable.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(final SelectionChangedEvent event) {
                databaseSelectionChanged();
            }
        });
        
        datasetTable.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                viewDataset();
            }
        });

        Group corehunterRunArgumentsGroup = new Group(parent, SWT.BORDER | SWT.SHADOW_IN);
        corehunterRunArgumentsGroup.setLayout(new GridLayout(5, false));
        corehunterRunArgumentsGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        corehunterRunArgumentsGroup.setText("Core Hunter Arguments");

        lblDatasetSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblDatasetSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblCoreSize = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblCoreSize.setText("Core Size");
        lblCoreSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));

        spinnerSize = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);

        spinnerSize.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerSizeUpdated();
            }
        });

        Label lblIntensity = new Label(corehunterRunArgumentsGroup, SWT.NONE);
        lblIntensity.setText("Intensity");

        spinnerIntensity = new Spinner(corehunterRunArgumentsGroup, SWT.BORDER);
        spinnerIntensity.setMinimum(0);
        spinnerIntensity.setSelection(20); // TODO get from properties file
        spinnerIntensity.setMaximum(100);

        spinnerIntensity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                spinnerIntensityUpdated();
            }
        });
        
        table = new Table(corehunterRunArgumentsGroup, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        btnAddObjective = new Button(corehunterRunArgumentsGroup, SWT.NONE);
        btnAddObjective.setText("Add Objective");
        
        btnRemoveObjective = new Button(corehunterRunArgumentsGroup, SWT.NONE);
        btnRemoveObjective.setText("Remove Objective");
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);
        new Label(corehunterRunArgumentsGroup, SWT.NONE);

        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        btnStart = new Button(composite, SWT.NONE);
        btnStart.setText("Start");

        btnStart.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                startCorehunterRun();
            }
        });

        btnReset = new Button(composite, SWT.NONE);
        btnReset.setText("Reset");

        btnReset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                resetArguments();
            }
        });

        updateDatasetButtons();
        updateDatasetSize();
        updateCorehunterArguments();
        updateStartButton();
    }

    private void updateViewer() {
        datasetTable.updateViewer();
    }

    protected void spinnerSizeUpdated() {
        spinnerIntensity.setSelection(getIntensityFromSize(selectedDatasetSize, spinnerSize.getSelection()));

    }

    protected void spinnerIntensityUpdated() {
        spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection()));

    }

    private void removeDataset() {
        try {
            this.getDatasetServices().removeDataset(selectedDataset.getUniqueIdentifier());
            updateViewer();
        } catch (DatasetException e) {
            // TODO Auto-generated catch block
            handleException(e);
        }
    }

    private void viewDataset() {
        partUtilitiies.openPart(new PartInput(selectedDataset, DatasetPart.ID));
    }

    private void addDataset() {

    }

    private void databaseSelectionChanged() {
        selectedDataset = datasetTable.getSelectedDataset();

        if (selectedDataset instanceof FeatureData)
            selectedDatasetSize = ((FeatureData) selectedDataset).getRowCount();
        else
            selectedDatasetSize = 0;

        updateDatasetSize();
        updateDatasetButtons();
        updateCorehunterArguments();
    }

    private void updateDatasetButtons() {
        btnRemoveDataset.setEnabled(datasetTable.getSelectedDataset() != null);
        btnView.setEnabled(datasetTable.getSelectedDataset() != null);
    }

    private void updateStartButton() {
        btnStart.setEnabled(datasetTable.getSelectedDataset() != null);
    }

    private void resetArguments() {
        datasetTable.cleaerSelectedDataset();
        selectedDatasetSize = 0;
        updateStartButton();
        updateDatasetButtons();
        updateCorehunterArguments();
    }

    private void updateDatasetSize() {
        lblDatasetSize.setText("Dataset Size : " + selectedDatasetSize);
    }

    private void updateCorehunterArguments() {
        if (selectedDatasetSize > 1) {
            spinnerSize.setMaximum(selectedDatasetSize - 1);
            spinnerSize.setSelection(getSizeFromIntensity(selectedDatasetSize, spinnerIntensity.getSelection()));

        } else {
            spinnerSize.setMinimum(0);
            spinnerSize.setSelection(0);
        }
    }

    private int getSizeFromIntensity(int datasetSize, int intensity) {
        return (int) ((double) datasetSize * ((double) intensity / 100.0));
    }

    private int getIntensityFromSize(int datasetSize, int coreSize) {
        return (int) (((double) coreSize / (double) datasetSize) * 100.0);
    }

    private void startCorehunterRun() {
        // TODO Auto-generated method stub

    }

    private void handleException(DatasetException e) {
        // TODO Auto-generated method stub

    }
}