/*!
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright (c) 2002-2013 Pentaho Corporation..  All rights reserved.
 */

package org.pentaho.mantle.client.solutionbrowser.filehistory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import org.pentaho.gwt.widgets.client.dialogs.PromptDialogBox;
import org.pentaho.gwt.widgets.client.filechooser.RepositoryFile;
import org.pentaho.mantle.client.messages.Messages;
import org.pentaho.mantle.client.solutionbrowser.SolutionBrowserPanel;
import org.pentaho.mantle.client.solutionbrowser.fileproperties.IFileModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class GeneralFileHistoryPanel extends FlexTable implements IFileModifier {

    Label nameLabel = new Label();

    Label locationLabel = new Label();

    Label sourceLabel = new Label();

    Label typeLabel = new Label();

    Label sizeLabel = new Label();

    Label createdLabel = new Label();

    Label lastModifiedDateLabel = new Label();

    Label deletedDateLabel = new Label();

    Label originalLocationLabel = new Label();

    Label ownerLabel = new Label();

    // ---------- new version history labels
    Label authorLabel = new Label(Messages.getString("authorLabel"));
    Label dateLabel = new Label(Messages.getString("dateLabel"));
    Label versionNumberLabel = new Label(Messages.getString("versionNumberLabel"));
    Label versionedFileIdLabel = new Label(Messages.getString("versionedFileIdLabel"));
    Label versionMessageLabel = new Label(Messages.getString("versionMessageLabel"));

    // IFileSummary fileSummary;
    RepositoryFile fileSummary;

    boolean isInTrash;

    boolean dirty = false;

    ArrayList<JSONObject> metadataPerms = new ArrayList<JSONObject>();

    VerticalPanel metadataPermsPanel = new VerticalPanel();

    private static final String VERSION_LIST_ITEM_ELEMENT_NAME = "list"; //$NON-NLS-1$
    private static final String VERSION_AUTHOR_NODE_NAME = "author"; //$NON-NLS-1$
    private static final String VERSION_DATE_NODE_NAME = "date"; //$NON-NLS-1$
    private static final String VERSION_NUMBER_NODE_NAME = "id"; //$NON-NLS-1$
    private static final String VERSION_FILE_ID_NODE_NAME = "versionedFileId"; //$NON-NLS-1$
    private static final String VERSION_MESSAGE_NODE_NAME = "message"; //$NON-NLS-1$

    /**
     * @param dialog
     * @param fileSummary
     */
    public GeneralFileHistoryPanel(final PromptDialogBox dialog, final RepositoryFile fileSummary) {
        super();
        this.fileSummary = fileSummary;
        isInTrash = this.fileSummary.getPath().contains("/.trash/pho:"); //$NON-NLS-1$
        setWidget(0, 0, authorLabel); //$NON-NLS-1$
        setWidget(0, 1, dateLabel); //$NON-NLS-1$
        setWidget(0, 2, versionNumberLabel); //$NON-NLS-1$
        setWidget(0, 3, versionedFileIdLabel); //$NON-NLS-1$
        setWidget(0, 4, versionMessageLabel); //$NON-NLS-1$

        setCellPadding(2);
        setCellSpacing(2);

        init();
    }

    /**
     *
     */
    public void apply() {
        // not used
    }

    public List<RequestBuilder> prepareRequests() {
        ArrayList<RequestBuilder> requestBuilders = new ArrayList<RequestBuilder>();
        String moduleBaseURL = GWT.getModuleBaseURL();
        String moduleName = GWT.getModuleName();
        String contextURL = moduleBaseURL.substring(0, moduleBaseURL.lastIndexOf(moduleName));
        String setMetadataUrl =
                contextURL
                        + "api/repo/files/" + SolutionBrowserPanel.pathToId(fileSummary.getPath()) + "/metadata?cb=" + System.currentTimeMillis(); //$NON-NLS-1$//$NON-NLS-2$
        RequestBuilder setMetadataBuilder = new RequestBuilder(RequestBuilder.PUT, setMetadataUrl);
        setMetadataBuilder.setHeader("Content-Type", "application/json");
        setMetadataBuilder.setHeader("If-Modified-Since", "01 Jan 1970 00:00:00 GMT");

        // prepare request data
        JSONArray arr = new JSONArray();
        JSONObject metadata = new JSONObject();
        metadata.put("stringKeyStringValueDto", arr);
        for (int i = 0; i < metadataPerms.size(); i++) {
            Set<String> keys = metadataPerms.get(i).keySet();
            for (String key : keys) {
                if (key != null && SolutionBrowserPanel.getInstance().isAdministrator()) {
                    if (key.equals("_PERM_SCHEDULABLE") && !fileSummary.isFolder() || key.equals("_PERM_HIDDEN")) {
                        JSONObject obj = new JSONObject();
                        obj.put("key", new JSONString(key));
                        obj.put("value", metadataPerms.get(i).get(key).isString());
                        arr.set(i, obj);
                    }
                }
            }
        }
        // setMetadataBuilder.sendRequest(metadata.toString(), setMetadataCallback);
        if (arr.size() > 0) {
            setMetadataBuilder.setRequestData(metadata.toString());
            requestBuilders.add(setMetadataBuilder);
        }

        return requestBuilders;
    }

    /**
     *
     */
    public void init() {
        nameLabel.setText(fileSummary.getTitle());
        typeLabel.setText(fileSummary.isFolder()
                ? Messages.getString("folder") : fileSummary.getName().substring(fileSummary.getName().lastIndexOf("."))); //$NON-NLS-1$//$NON-NLS-2$
        sourceLabel.setText(isInTrash ? Messages.getString("recycleBin") : fileSummary.getPath()); //$NON-NLS-1$//$NON-NLS-2$
        locationLabel
                .setText(isInTrash
                        ? Messages.getString("recycleBin") : fileSummary.getPath().substring(0, fileSummary.getPath().lastIndexOf("/"))); //$NON-NLS-1$//$NON-NLS-2$
        sizeLabel.setText(NumberFormat.getDecimalFormat().format(fileSummary.getFileSize() / 1000.00)
                + " " + Messages.getString("kiloBytes")); //$NON-NLS-1$ //$NON-NLS-2$
        createdLabel.setText(fileSummary.getCreatedDate().toString());
        lastModifiedDateLabel.setText(fileSummary.getLastModifiedDate() == null ? fileSummary.getCreatedDate().toString()
                : fileSummary.getLastModifiedDate().toString());
        deletedDateLabel.setText(fileSummary.getDeletedDate() == null ? "" : fileSummary.getDeletedDate().toString()); //$NON-NLS-1$
        originalLocationLabel.setText(fileSummary.getOriginalParentFolderPath());
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.pentaho.mantle.client.solutionbrowser.fileproperties.IFileModifier#init(org.pentaho.platform.repository2
     * .unified.webservices.RepositoryFileDto, com.google.gwt.xml.client.Document)
     */
    @Override
    public void init(RepositoryFile fileSummary, Document fileInfo) {
        // TODO Auto-generated method stub

    }

    /**
     * Add an hr element with a specified colspan
     *
     * @param row
     * @param col
     */
    @SuppressWarnings("serial")
    protected void addHr(int row, int col, int colspan) {
        setHTML(row, col, new SafeHtml() {
            @Override
            public String asString() {
                return "<hr/>";
            }
        });
        getFlexCellFormatter().setColSpan(row, col, colspan);
    }

    /**
     * Get versions from response
     *
     * @param response
     */
    public void setVersionsResponse(Response response) {
        Document versions = XMLParser.parse(response.getText());
        NodeList versionNodes = versions.getElementsByTagName(VERSION_LIST_ITEM_ELEMENT_NAME);
        int rowCount = 1;
        int nodeCount = 0;

        for (int x = 0; x <= versionNodes.getLength() - 1; x++) {
            String authorNodeValue = null;
            String dateNodeValue = null;
            String versionNumberNodeValue = null;
            String versionIdNodeValue = null;
            String versionMessageNodeValue = null;
            Node thisVersionNode = versionNodes.item(x);
            NodeList versionChildNodes = thisVersionNode.getChildNodes();
            for (int y = 0; y <= versionChildNodes.getLength() - 1; y++) {
                Node thisVersionChildNode = versionChildNodes.item(y);
                if (thisVersionChildNode.getNodeName().equalsIgnoreCase(VERSION_AUTHOR_NODE_NAME)) {
                    authorNodeValue = thisVersionChildNode.getFirstChild().getNodeValue();
                } else if (thisVersionChildNode.getNodeName().equalsIgnoreCase(VERSION_DATE_NODE_NAME)) {
                    dateNodeValue = thisVersionChildNode.getFirstChild().getNodeValue();
                } else if (thisVersionChildNode.getNodeName().equalsIgnoreCase(VERSION_NUMBER_NODE_NAME)) {
                    versionNumberNodeValue = thisVersionChildNode.getFirstChild().getNodeValue();
                } else if (thisVersionChildNode.getNodeName().equalsIgnoreCase(VERSION_FILE_ID_NODE_NAME)) {
                    versionIdNodeValue = thisVersionChildNode.getFirstChild().getNodeValue();
                } else if (thisVersionChildNode.getNodeName().equalsIgnoreCase(VERSION_MESSAGE_NODE_NAME)) {
                    versionMessageNodeValue = thisVersionChildNode.getFirstChild().getNodeValue();
                }
                if (
                        (authorNodeValue != null)
                                && (dateNodeValue != null)
                                && (versionNumberNodeValue != null)
                                && (versionIdNodeValue != null)) {
                    setWidget(rowCount, 0, new Label(authorNodeValue.toString()));
                    setWidget(rowCount, 1, new Label(dateNodeValue.toString()));
                    setWidget(rowCount, 2, new Label(versionNumberNodeValue.toString()));
                    setWidget(rowCount, 3, new Label(versionIdNodeValue.toString()));
                    if( versionMessageNodeValue != null ) {
                        setWidget(rowCount, 4, new Label(versionMessageNodeValue.toString()));
                    }
                    Button restoreButton = new Button( Messages.getString( "versionRestoreButtonLabel" ));
                    restoreButton.setEnabled(Boolean.FALSE);
                    restoreButton.setStylePrimaryName("pentaho-button");
                    setWidget(rowCount, 5, restoreButton); //$NON-NLS-1$
                    
                    Button downloadButton = new Button( Messages.getString("versionDownloadButtonLabel"));
                    downloadButton.setEnabled(Boolean.FALSE);
                    downloadButton.setStylePrimaryName("pentaho-button");
                    setWidget(rowCount, 6, downloadButton); //$NON-NLS-1$
                    
                    rowCount++;
                }
            }
        }
    }
}