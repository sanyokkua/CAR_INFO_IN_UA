package ua.kostenko.carinfo.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import ua.kostenko.carinfo.csv.processors.AdminUnitUpperCaseProcessor;
import ua.kostenko.carinfo.csv.records.AdministrativeUnitRecord;

@Configuration
public class AdminConfig {

    protected static final String[] ADMIN_UNIT_HEADERS = {
            "code", "type", "name"
    };

    @Value("classPath:KOATUU_03072018.csv")
    private Resource adminUnitCsvResource;

    @Bean
    public FlatFileItemReader<AdministrativeUnitRecord> adminUnitRecordReader() {
        BeanWrapperFieldSetMapper<AdministrativeUnitRecord> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(AdministrativeUnitRecord.class);

        return new FlatFileItemReaderBuilder<AdministrativeUnitRecord>()
                .name("adminUnitRecordReader")
                .resource(adminUnitCsvResource)
                .delimited()
                .delimiter(";")
                .names(ADMIN_UNIT_HEADERS)
                .fieldSetMapper(mapper)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public AdminUnitUpperCaseProcessor adminUnitRecordProcessor() {
        return new AdminUnitUpperCaseProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<AdministrativeUnitRecord> adminUnitRecordWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<AdministrativeUnitRecord>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO car_info.admin_unit (code, type, name) VALUES (:code, :type, :name)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importAdministrativeUnitRecordJob(
            @Qualifier("adminUnitJobNotificationListener") JobExecutionListener listener,
            Step adminUnitProcessingStep,
            JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("importAdministrativeUnitRecordJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(adminUnitProcessingStep)
                .end()
                .build();
    }

    @Bean
    public Step adminUnitProcessingStep(JdbcBatchItemWriter<AdministrativeUnitRecord> writer,
            StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("adminUnitProcessingStep")
                .<AdministrativeUnitRecord, AdministrativeUnitRecord>chunk(10)
                .reader(adminUnitRecordReader())
                .processor(adminUnitRecordProcessor())
                .writer(writer)
                .build();
    }

}
