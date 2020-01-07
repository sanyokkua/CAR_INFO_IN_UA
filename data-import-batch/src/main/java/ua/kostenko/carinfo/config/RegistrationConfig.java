package ua.kostenko.carinfo.config;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
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
import org.springframework.core.io.Resource;
import ua.kostenko.carinfo.csv.processors.RegistrationRecordProcessor;
import ua.kostenko.carinfo.csv.records.RegistrationRecord;
import ua.kostenko.carinfo.jobs.RegistrationJobNotificationListener;

//@Configuration
public class RegistrationConfig {

    protected static final String[] REGISTRATION_HEADERS = {
            "person", "reg_addr_koatuu", "oper_code", "oper_name", "d_reg", "dep_code",
            "dep", "brand", "model", "make_year", "color", "kind",
            "body", "purpose", "fuel", "capacity", "own_weight", "total_weight", "n_reg_new"
    };

    //    @Value("classPath:/input/inputData.csv")
    private Resource inputResource;

    //    @Bean
    public FlatFileItemReader<RegistrationRecord> registrationRecordReader() {
        BeanWrapperFieldSetMapper<RegistrationRecord> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(RegistrationRecord.class);
        return new FlatFileItemReaderBuilder<RegistrationRecord>()
                .name("registrationCsvReader")
                .resource(inputResource)
                .delimited()
                .names(REGISTRATION_HEADERS)
                .fieldSetMapper(mapper)
                .build();
    }

    //    @Bean
    public RegistrationRecordProcessor registrationRecordProcessor() {
        return new RegistrationRecordProcessor();
    }

    //    @Bean
    public JdbcBatchItemWriter<RegistrationRecord> registrationRecordWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<RegistrationRecord>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
                .dataSource(dataSource)
                .build();
    }

    //    @Bean
    public Job importRegistrationRecordJob(RegistrationJobNotificationListener listener, Step step1,
            JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("importRegistrationRecordJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    //    @Bean
    public Step step1(JdbcBatchItemWriter<RegistrationRecord> writer, StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("step1")
                .<RegistrationRecord, RegistrationRecord>chunk(10)
                .reader(registrationRecordReader())
                .processor(registrationRecordProcessor())
                .writer(writer)
                .build();
    }
}
