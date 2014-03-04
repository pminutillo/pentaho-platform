/**
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
 * Copyright 2006 - 2014 Pentaho Corporation.  All rights reserved.
 *
 */

package org.pentaho.platform.repository2.unified;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.platform.api.repository2.unified.IRepositoryFileData;
import org.pentaho.platform.api.repository2.unified.RepositoryFile;
import org.pentaho.platform.api.repository2.unified.RepositoryFileAcl;

import java.util.Date;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultUnifiedRepositoryMocksTest {

  private DefaultUnifiedRepository defaultUnifiedRepository;

  private IRepositoryFileDao mockRepositoryFileDao;

  private IRepositoryFileAclDao mockRepositoryFileAclDao;

  private static final String CREATOR_ID = "admin";
  private static final String TEST_FILE_1_ID = "ID0000001";
  private static final String TEST_FILE_1_TITLE = "TEST FILE 1 TITLE";
  private static final String TEST_FILE_1_DESC = "TEST FILE 1 DESC";
  private static final String TEST_FILE_1_FILENAME = "testFile1.txt";
  private static final String TEST_FILE_1_ENCODING = "en_US";
  private static final String TEST_FILE_1_PATH = "/path/to/file1";
  private static final String TEST_FILE_1_VERSION_ID = "1.0";

  private static final String TEST_FOLDER_1_ID = "ID0000001";
  private static final String TEST_FOLDER_1_TITLE = "TEST FOLDER 1 TITLE";
  private static final String TEST_FOLDER_1_DESC = "TEST FOLDER 1 DESC";
  private static final String TEST_FOLDER_1_FOLDERNAME = "testFOLDER1.txt";
  private static final String TEST_FOLDER_1_ENCODING = "en_US";
  private static final String TEST_FOLDER_1_PATH = "/path/to/FOLDER1";
  
  private static final String TEST_FOLDER_2_PATH = "/path/to/FOLDER2";
  
  private static final String TEST_FILE_2_FILENAME = "testFile2.txt";
  private static final String TEST_FILE_2_PATH = "/path/to/file2";
  
  
  private static final String VERSION_MESSAGE = "";

  RepositoryFile repositoryFile1;
  RepositoryFile repositoryFolder1;
  IRepositoryFileData mockRepositoryFile1Data;
  RepositoryFileAcl repositoryFileAcl;

  @Before
  public void setUp() throws Exception {
    repositoryFile1 = new RepositoryFile(TEST_FILE_1_ID, TEST_FILE_1_FILENAME, false, false, false,
        null, TEST_FILE_1_PATH, new Date(), new Date(), false, null, null, null, TEST_FILE_1_ENCODING, TEST_FILE_1_TITLE,
        TEST_FILE_1_DESC, TEST_FILE_2_PATH, new Date(), 4, CREATOR_ID, new HashMap());

    repositoryFolder1 = new RepositoryFile(TEST_FOLDER_1_ID, TEST_FOLDER_1_FOLDERNAME, true, false, false,
        null, TEST_FOLDER_1_PATH, new Date(), new Date(), false, null, null, null, TEST_FOLDER_1_ENCODING, TEST_FOLDER_1_TITLE,
        TEST_FOLDER_1_DESC, TEST_FOLDER_2_PATH, new Date(), 4, CREATOR_ID, new HashMap());
    
    mockRepositoryFile1Data = mock(IRepositoryFileData.class);

    repositoryFileAcl = mock(RepositoryFileAcl.class);

    mockRepositoryFileDao = mock(IRepositoryFileDao.class);
    when(mockRepositoryFileDao.canUnlockFile(TEST_FILE_1_FILENAME)).thenReturn(true);
    when(mockRepositoryFileDao.canUnlockFile(TEST_FILE_2_FILENAME)).thenReturn(false);
    when(mockRepositoryFileDao.createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, null, VERSION_MESSAGE)).thenReturn(repositoryFile1);
    when(mockRepositoryFileDao.createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, repositoryFileAcl, VERSION_MESSAGE)).thenReturn(repositoryFile1);
    when(mockRepositoryFileDao.createFolder(TEST_FOLDER_1_ID, repositoryFolder1, null, VERSION_MESSAGE)).thenReturn(repositoryFolder1);
    when(mockRepositoryFileDao.createFolder(TEST_FOLDER_1_ID, repositoryFolder1, repositoryFileAcl, VERSION_MESSAGE)).thenReturn(repositoryFolder1);

    when(mockRepositoryFileDao.createFile(TEST_FOLDER_1_ID, repositoryFolder1, mockRepositoryFile1Data, null, VERSION_MESSAGE)).thenReturn(repositoryFile1);
    when(mockRepositoryFileDao.createFile(TEST_FOLDER_1_ID, repositoryFolder1, mockRepositoryFile1Data, repositoryFileAcl, VERSION_MESSAGE)).thenReturn(repositoryFile1);

    mockRepositoryFileAclDao = mock(IRepositoryFileAclDao.class);
    when(mockRepositoryFileAclDao.getAcl(TEST_FILE_1_ID)).thenReturn(repositoryFileAcl);

    defaultUnifiedRepository = new DefaultUnifiedRepository(mockRepositoryFileDao, mockRepositoryFileAclDao);
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testGetEffectiveAces() throws Exception {

  }

  @Test
  public void testGetEffectiveAces2() throws Exception {

  }

  @Test
  public void testHasAccess() throws Exception {

  }

  @Test
  public void testGetFile() throws Exception {

  }

  @Test
  public void testGetFileById() throws Exception {

  }

  @Test
  public void testGetFile2() throws Exception {

  }

  @Test
  public void testGetFileById2() throws Exception {

  }

  @Test
  public void testGetFile3() throws Exception {

  }

  @Test
  public void testGetFileById3() throws Exception {

  }

  @Test
  public void testGetFile4() throws Exception {

  }

  @Test
  public void testGetFileById4() throws Exception {

  }

  @Test
  public void testCreateFile() throws Exception {
    RepositoryFile createdFile = defaultUnifiedRepository.createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, null, VERSION_MESSAGE);
    assert createdFile.getName().equals(TEST_FILE_1_FILENAME);
    assert createdFile.getPath().equals(TEST_FILE_1_PATH);
    verify(mockRepositoryFileDao).createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, null, VERSION_MESSAGE);
  }

  @Test
  public void testCreateFolder() throws Exception {
    RepositoryFile createdFile = defaultUnifiedRepository.createFolder(TEST_FOLDER_1_ID, repositoryFolder1, null, VERSION_MESSAGE);
    assert createdFile.getName().equals(TEST_FOLDER_1_FOLDERNAME);
    assert createdFile.getPath().equals(TEST_FOLDER_1_PATH);
    verify(mockRepositoryFileDao).createFolder(TEST_FOLDER_1_ID, repositoryFolder1, null, VERSION_MESSAGE);
  }

  @Test
  public void testCreateFileWithAcls() throws Exception {
    RepositoryFile createdFile = defaultUnifiedRepository.createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, repositoryFileAcl, VERSION_MESSAGE);
    assert createdFile.getName().equals(TEST_FILE_1_FILENAME);
    assert createdFile.getPath().equals(TEST_FILE_1_PATH);
    verify(mockRepositoryFileDao).createFile(TEST_FILE_1_ID, repositoryFile1, mockRepositoryFile1Data, repositoryFileAcl, VERSION_MESSAGE);
  }

  @Test
  public void testCreateFolderWithAcls() throws Exception {
    RepositoryFile createdFile = defaultUnifiedRepository.createFolder(TEST_FOLDER_1_ID, repositoryFolder1, repositoryFileAcl, VERSION_MESSAGE);
    assert createdFile.getName().equals(TEST_FOLDER_1_FOLDERNAME);
    assert createdFile.getPath().equals(TEST_FOLDER_1_PATH);
    verify(mockRepositoryFileDao).createFolder(TEST_FOLDER_1_ID, repositoryFolder1, repositoryFileAcl, VERSION_MESSAGE);
  }

  @Test
  public void testGetDataForExecute() throws Exception {

  }

  @Test
  public void testGetDataAtVersionForExecute() throws Exception {

  }

  @Test
  public void testGetDataForRead() throws Exception {

  }

  @Test
  public void testGetDataAtVersionForRead() throws Exception {

  }

  @Test
  public void testGetDataForReadInBatch() throws Exception {

  }

  @Test
  public void testGetDataForExecuteInBatch() throws Exception {

  }

  @Test
  public void testGetChildren() throws Exception {

  }

  @Test
  public void testGetChildren2() throws Exception {

  }

  @Test
  public void testGetChildren3() throws Exception {

  }

  @Test
  public void testGetChildren4() throws Exception {

  }

  @Test
  public void testUpdateFile() throws Exception {

  }

  @Test
  public void testDeleteFile() throws Exception {
    defaultUnifiedRepository.deleteFile(TEST_FILE_1_ID, VERSION_MESSAGE);
    verify(mockRepositoryFileDao).deleteFile(TEST_FILE_1_ID, VERSION_MESSAGE);
  }

  @Test
  public void testDeleteFilePermanent() throws Exception {
    defaultUnifiedRepository.deleteFile(TEST_FILE_1_ID, true, VERSION_MESSAGE);
    verify(mockRepositoryFileDao).permanentlyDeleteFile(TEST_FILE_1_ID, VERSION_MESSAGE);
  }

  @Test
  public void testDeleteFileAtVersion() throws Exception {
    mockRepositoryFileDao.deleteFileAtVersion(TEST_FILE_1_ID, TEST_FILE_1_VERSION_ID);
  }

  @Test
  public void testGetDeletedFiles() throws Exception {

  }

  @Test
  public void testGetDeletedFiles2() throws Exception {

  }

  @Test
  public void testGetDeletedFiles3() throws Exception {

  }

  @Test
  public void testUndeleteFile() throws Exception {

  }

  @Test
  public void testGetAcl() throws Exception {
    RepositoryFileAcl testFileAcl = defaultUnifiedRepository.getAcl(TEST_FILE_1_ID);
    assert(testFileAcl.equals(repositoryFileAcl));
    verify(mockRepositoryFileAclDao).getAcl(TEST_FILE_1_ID);
  }

  @Test
  public void testLockFile() throws Exception {

  }

  @Test
  public void testUnlockFile() throws Exception {

  }

  @Test
  public void testGetVersionSummary() throws Exception {

  }

  @Test
  public void testGetVersionSummaryInBatch() throws Exception {

  }

  @Test
  public void testGetVersionSummaries() throws Exception {

  }

  @Test
  public void testGetFileAtVersion() throws Exception {

  }

  @Test
  public void testUpdateAcl() throws Exception {

  }

  @Test
  public void testMoveFile() throws Exception {

  }

  @Test
  public void testCopyFile() throws Exception {
    defaultUnifiedRepository.copyFile(TEST_FILE_1_FILENAME, TEST_FILE_2_PATH, VERSION_MESSAGE);
    verify(mockRepositoryFileDao).copyFile(TEST_FILE_1_FILENAME, TEST_FILE_2_PATH, VERSION_MESSAGE);
  }

  @Test
  public void testRestoreFileAtVersion() throws Exception {

  }

  @Test
  public void testCanUnlockFile() throws Exception {
    assert(defaultUnifiedRepository.canUnlockFile(TEST_FILE_1_FILENAME) == true);
    verify(mockRepositoryFileDao.canUnlockFile(TEST_FILE_1_FILENAME));
  }

  @Test
  public void testCanUnlockFileFalse() throws Exception {
    assert(defaultUnifiedRepository.canUnlockFile(TEST_FILE_2_FILENAME) == false);
    verify(mockRepositoryFileDao.canUnlockFile(TEST_FILE_2_FILENAME));
  }

  @Test
  public void testGetTree() throws Exception {

  }

//  @Test
//  public void testGetTree() throws Exception {
//
//  }

  @Test
  public void testGetReferrers() throws Exception {

  }

  @Test
  public void testSetFileMetadata() throws Exception {

  }

  @Test
  public void testGetFileMetadata() throws Exception {

  }

  @Test
  public void testGetReservedChars() throws Exception {

  }

  @Test
  public void testGetAvailableLocalesForFileById() throws Exception {

  }

  @Test
  public void testGetAvailableLocalesForFileByPath() throws Exception {

  }

  @Test
  public void testGetAvailableLocalesForFile() throws Exception {

  }

  @Test
  public void testGetLocalePropertiesForFileById() throws Exception {

  }

  @Test
  public void testGetLocalePropertiesForFileByPath() throws Exception {

  }

  @Test
  public void testGetLocalePropertiesForFile() throws Exception {

  }

  @Test
  public void testSetLocalePropertiesForFileById() throws Exception {

  }

  @Test
  public void testSetLocalePropertiesForFileByPath() throws Exception {

  }

  @Test
  public void testSetLocalePropertiesForFile() throws Exception {

  }

  @Test
  public void testDeleteLocalePropertiesForFile() throws Exception {
    defaultUnifiedRepository.deleteLocalePropertiesForFile(repositoryFile1, TEST_FILE_1_ENCODING);
    verify(mockRepositoryFileDao).deleteLocalePropertiesForFile(repositoryFile1, TEST_FILE_1_ENCODING);
  }

  @Test
  public void testUpdateFolder() throws Exception {

  }
}
