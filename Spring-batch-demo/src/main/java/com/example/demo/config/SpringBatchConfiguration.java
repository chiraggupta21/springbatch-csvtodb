package com.example.demo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.demo.model.User;

@Configuration
public class SpringBatchConfiguration {

	
	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory,
					StepBuilderFactory stepBuilderFactory,
					ItemReader<User> itemReader
					,ItemProcessor<User, User> itemProcessor,
					ItemWriter<User> itemWriter) {
		
		Step step=stepBuilderFactory.get("ETL-file-load")
				.<User,User>chunk(100)
				.reader(itemReader)
				.processor(itemProcessor)
				.writer(itemWriter)
				.build();
		
		return jobBuilderFactory.get("ETL-Load")
				.incrementer(new RunIdIncrementer())
				.start(step)
				.build();
	}
	
	@Bean 
	FlatFileItemReader<User> fileItemReader(@Value("${fileLocation}") Resource resource){
		FlatFileItemReader<User> flatFileItemReader=new FlatFileItemReader<>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-Reader");
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		System.out.println(resource);
		System.out.println(lineMapper());
		return flatFileItemReader;
	}
	
	@Bean
	public LineMapper<User> lineMapper() {
		DelimitedLineTokenizer linetokenizer=new DelimitedLineTokenizer();
		linetokenizer.setDelimiter(",");
		linetokenizer.setStrict(false);
		linetokenizer.setNames(new String[] {"id","name","Department"});
		BeanWrapperFieldSetMapper<User> beanWrapperFieldSetMapper= new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(User.class);
		
		DefaultLineMapper<User> defaultLineMapper=new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(linetokenizer);
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;
	}
}
