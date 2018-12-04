package com.springbatch.demo;

import java.util.Date;

import org.springframework.batch.item.ItemProcessor;
import com.springbatch.demo.domain.Item;

public class ItemsProcesser implements ItemProcessor<Item, Item> {

	@Override
	public Item process(final Item item) throws Exception {
		final String upperName = item.getName().toUpperCase();
		final Integer price = item.getPrice();
		final Date date = item.getExpiration_date();
		final Integer amount = item.getAmount();
		
		final Item proceccedItem = new Item(upperName, price, date, amount);
		return proceccedItem;
	}
}