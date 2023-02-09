package com.lexisnexis.tms.config;

import com.lexisnexis.tms.entity.WorkHistory;
import com.lexisnexis.tms.repository.WorkHistoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@EnableBatchProcessing
@Configuration
public class ExportJobConfig {

    @Autowired
    WorkHistoryRepository workHistoryRepository;

    @Bean
    public Job exporterJob(Step exportStep, JobRepository jobRepository){
        return new JobBuilder("someJob")
                .listener(exporterJobListener())
                .start(exportStep)
                .repository(jobRepository)
                .build();
    }

    @Bean
    public JobExecutionListener exporterJobListener(){
        return new JobExecutionListener() {

            final Logger logger = LogManager.getLogger(this.getClass().getName());

            @Override
            public void beforeJob(JobExecution jobExecution) {
                logger.info("starting batch job to write all work history to a csv file");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                logger.info("writing to csv complete, check target/classes/work-history.csv for updates");
            }
        };
    }

    @Bean
    public Step exportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilder("exportStep")
                .chunk(10)
                .reader(repositoryItemReader())
                .processor((Function<? super Object, ?>) (o) ->  {
                                WorkHistory workHistory = (WorkHistory) o;
                                return workHistory.getFlatFileRow();
                            }
                )
                .writer(flatFileItemWriter())
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public RepositoryItemReader<?> repositoryItemReader(){
        return new RepositoryItemReaderBuilder<>()
                .repository(workHistoryRepository)
                .methodName("findAll")
                .sorts(Map.of("loginAt", Sort.Direction.DESC))
                .name("db-reader")
                .build();
    }

    @Bean
    public ItemWriter<? super Object> flatFileItemWriter(){
        return new FlatFileItemWriterBuilder<>()
                .lineAggregator(new DelimitedLineAggregator<>())
                .resource(new ClassPathResource("work-history.csv"))
                .name("csv-writer")
                .headerCallback((writer) ->
                        writer.append(Arrays.stream(
                                WorkHistory.class.getDeclaredFields()).map(Field::getName).reduce((x, y) -> x + "," + y).get()
                        )
                )
//                .resource(new FileSystemResource("C:\\Users\\SinghP20\\OneDrive - Reed Elsevier Group ICO Reed Elsevier Inc\\Documents\\work-history.csv"))   can use it to write to an absolute path
                .build();
    }
}
