package org.recap.service.executor.datadump;

import org.recap.ReCAPConstants;
import org.recap.model.export.DataDumpRequest;
import org.recap.model.export.FullDataDumpCallable;
import org.recap.model.export.IncrementalDataDumpCallable;
import org.recap.model.jaxb.marc.BibRecords;
import org.recap.repository.BibliographicDetailsRepository;
import org.recap.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by premkb on 27/9/16.
 */
@Service
@Scope("prototype")
public class IncrementalDataDumpExecutorService  extends AbstractDataDumpExecutorService{

    private static final Logger logger = LoggerFactory.getLogger(IncrementalDataDumpExecutorService.class);

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private BibliographicDetailsRepository bibliographicDetailsRepository;

    @Override
    public boolean isInterested(String fetchType) {
        return fetchType.equals(ReCAPConstants.DATADUMP_FETCHTYPE_INCREMENTAL) ? true:false;
    }


    @Override
    public Callable getCallable(int pageNum, int batchSize, DataDumpRequest dataDumpRequest, BibliographicDetailsRepository bibliographicDetailsRepository) {
        Callable callable = appContext.getBean(IncrementalDataDumpCallable.class,pageNum,batchSize,dataDumpRequest,bibliographicDetailsRepository);
        return callable;
    }

    @Override
    public Long getTotalRecordsCount(DataDumpRequest dataDumpRequest) {
        Date inputDate = DateUtil.getDateFromString(dataDumpRequest.getDate(), ReCAPConstants.DATE_FORMAT_YYYYMMDDHHMM);
        Long totalRecordCount = bibliographicDetailsRepository.countRecordsForIncrementalDump(dataDumpRequest.getCollectionGroupIds(), dataDumpRequest.getInstitutionCodes(),inputDate);
        return totalRecordCount;
    }

}
