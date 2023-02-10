package com.lexisnexis.tms.serviceimpl;

import com.lexisnexis.tms.BaseTest;
import com.lexisnexis.tms.entity.UserEntity;
import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.exception.UserNotFoundException;
import com.lexisnexis.tms.services.UserService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


class UserPdfExporterImplTest implements BaseTest {

    @InjectMocks
    UserPdfExporterImpl userPdfExporter;

    @Mock
    UserService userService;

    @Captor
    ArgumentCaptor<Element> elementCaptor;

    @BeforeAll
    public void init() {
        ReflectionTestUtils.setField(userPdfExporter, "async", false);
    }

    @Test
    public void verifyPdfContent(WorkHistory workHistory, UserEntity user) throws UserNotFoundException {

        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        given(userService.getDataByUserName(workHistory.getUserName())).willReturn(user);
        MockedConstruction<Document> mockedDocumentConstruction = mockConstruction(Document.class);

        userPdfExporter.export(mockHttpServletResponse, Collections.singletonList(workHistory));

        verify(userService).getDataByUserName(workHistory.getUserName());
        Document mockDocument = mockedDocumentConstruction.constructed().get(0);
        verify(mockDocument, times(2)).add(elementCaptor.capture());

        PdfPTable pdfPTable = (PdfPTable) elementCaptor.getValue();
        assertEquals("UserEntity Name", pdfPTable.getRow(0).getCells()[0].getPhrase().getContent());
        assertEquals("Working Area", pdfPTable.getRow(0).getCells()[1].getPhrase().getContent());
        assertEquals("Comments", pdfPTable.getRow(0).getCells()[2].getPhrase().getContent());
        assertEquals("Location", pdfPTable.getRow(0).getCells()[3].getPhrase().getContent());
        assertEquals(workHistory.getUserName(), pdfPTable.getRow(1).getCells()[0].getPhrase().getContent());
        assertEquals(workHistory.getWorkingArea(), pdfPTable.getRow(1).getCells()[1].getPhrase().getContent());
        assertEquals(workHistory.getComments(), pdfPTable.getRow(1).getCells()[2].getPhrase().getContent());
        assertEquals(user.getLocation(), pdfPTable.getRow(1).getCells()[3].getPhrase().getContent());

        mockedDocumentConstruction.close();
    }

    @Test
    public void verifyPdfContentWhenAsyncIsTrue(WorkHistory workHistory, UserEntity user) throws UserNotFoundException, IOException {

        ReflectionTestUtils.setField(userPdfExporter, "async", true);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        given(userService.getUserByUserName(workHistory.getUserName())).willReturn(CompletableFuture.completedFuture(user));
        MockedConstruction<Document> mockedDocumentConstruction = mockConstruction(Document.class);

        userPdfExporter.export(mockHttpServletResponse, Collections.singletonList(workHistory));

        verify(userService).getUserByUserName(workHistory.getUserName());
        Document mockDocument = mockedDocumentConstruction.constructed().get(0);
        verify(mockDocument, times(2)).add(elementCaptor.capture());

        PdfPTable pdfPTable = (PdfPTable) elementCaptor.getValue();
        assertEquals("UserEntity Name", pdfPTable.getRow(0).getCells()[0].getPhrase().getContent());
        assertEquals("Working Area", pdfPTable.getRow(0).getCells()[1].getPhrase().getContent());
        assertEquals("Comments", pdfPTable.getRow(0).getCells()[2].getPhrase().getContent());
        assertEquals("Location", pdfPTable.getRow(0).getCells()[3].getPhrase().getContent());
        assertEquals(workHistory.getUserName(), pdfPTable.getRow(1).getCells()[0].getPhrase().getContent());
        assertEquals(workHistory.getWorkingArea(), pdfPTable.getRow(1).getCells()[1].getPhrase().getContent());
        assertEquals(workHistory.getComments(), pdfPTable.getRow(1).getCells()[2].getPhrase().getContent());
        assertEquals(user.getLocation(), pdfPTable.getRow(1).getCells()[3].getPhrase().getContent());

        mockedDocumentConstruction.close();
    }

    @Test
    public void interruptedExceptionFromCompletableFutureIsHandledSuccessfully(WorkHistory workHistory, UserEntity user) throws UserNotFoundException, ExecutionException, InterruptedException {

        CompletableFuture mockCompletableFuture = mock(CompletableFuture.class);
        ReflectionTestUtils.setField(userPdfExporter, "async", true);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        given(userService.getUserByUserName(workHistory.getUserName())).willReturn(mockCompletableFuture);
        given(mockCompletableFuture.get()).willThrow(new InterruptedException());

        assertDoesNotThrow(() -> userPdfExporter.export(mockHttpServletResponse, Collections.singletonList(workHistory)));
    }

    @Test
    public void executionExceptionFromCompletableFutureIsHandledSuccessfully(WorkHistory workHistory, UserEntity user) throws UserNotFoundException, ExecutionException, InterruptedException {

        CompletableFuture mockCompletableFuture = mock(CompletableFuture.class);
        ReflectionTestUtils.setField(userPdfExporter, "async", true);
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        given(userService.getUserByUserName(workHistory.getUserName())).willReturn(mockCompletableFuture);
        given(mockCompletableFuture.get()).willThrow(new ExecutionException(null));

        assertDoesNotThrow(() -> userPdfExporter.export(mockHttpServletResponse, Collections.singletonList(workHistory)));
    }

    @Test
    public void IOExceptionFromHttpResponseIsHandledSuccessfully(WorkHistory workHistory) {

        ReflectionTestUtils.setField(userPdfExporter, "async", true);
        MockHttpServletResponse mockHttpServletResponse = mock(MockHttpServletResponse.class);
        given(mockHttpServletResponse.getOutputStream()).willThrow(new IOException());

        assertDoesNotThrow(() -> userPdfExporter.export(mockHttpServletResponse, Collections.singletonList(workHistory)));
    }
}