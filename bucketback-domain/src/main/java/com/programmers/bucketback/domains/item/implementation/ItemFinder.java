package com.programmers.bucketback.domains.item.implementation;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.bucketback.domains.item.domain.Item;
import com.programmers.bucketback.domains.item.model.ItemInfo;
import com.programmers.bucketback.domains.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemFinder {

	private final ItemRepository itemRepository;

	public List<ItemInfo> getItemNamesByKeyword(final String keyword) {
		final String trimmedKeyword = keyword.trim();

		if (trimmedKeyword.isEmpty()) {
			return Collections.emptyList();
		}

		List<Item> items = itemRepository.findItemsByNameContains(trimmedKeyword);

		return items.stream()
			.map(item -> new ItemInfo(
				item.getId(),
				item.getName(),
				item.getPrice(),
				item.getImage())
			).toList();
	}
}
