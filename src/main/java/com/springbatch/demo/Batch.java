package com.springbatch.demo;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.springbatch.demo.domain.Item;

@Configuration
@EnableBatchProcessing
public class Batch {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	@Autowired
	public DataSource dataSource;

	// Reader
	@Bean
	public FlatFileItemReader<Item> reader() {
		FlatFileItemReader<Item> reader = new FlatFileItemReader<Item>();
		reader.setResource(new ClassPathResource("SpringBatchSampleData.csv"));
		reader.setLineMapper(new DefaultLineMapper<Item>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "name", "price", "expiration_date", "amount" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Item>() {
					{
						setTargetType(Item.class);
					}
				});
			}
		});

		return reader;
	}

	// Processor
	@Bean
	public ItemsProcesser processer() {
		return new ItemsProcesser();
	}

	// Writer
	@Bean
	public JdbcBatchItemWriter<Item> writer() {
		JdbcBatchItemWriter<Item> writer = new JdbcBatchItemWriter<Item>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Item>());
		writer.setSql(
				"INSERT INTO items(name, price, expiration_date, amount) VALUES(:name, :price, :expiration_date, :amount)");
		writer.setDataSource(dataSource);
		return writer;
	}

	//Listener
	@Bean
	public JobExecutionListener listener() {
		return new Listener(new JdbcTemplate(dataSource));
	}

	//ステップ
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step")
			.<Item, Item>chunk(10)
			.reader(reader())
			.processor(processer())
			.writer(writer())
			.build();
	}
	
	//ジョブ
	@Bean
	public Job testJob() {
		return jobBuilderFactory.get("testJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.flow(step())
				.end()
				.build();
	}

}
