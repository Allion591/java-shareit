package ru.practicum.shareit.request.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.Collection;

@Repository
public class ItemRequestRepositoryImpl implements ItemRequestRepository {
    ItemRequest itemRequest = new ItemRequest();

    @Override
    public ItemRequest save(ItemRequest itemRequest) {
        return itemRequest;
    }

    @Override
    public ItemRequest update(ItemRequest itemRequest) {
        return itemRequest;
    }

    @Override
    public ItemRequest getById(Long requestId) {
        return itemRequest;
    }

    @Override
    public Collection<ItemRequest> getAll() {
        return new ArrayList<>();
    }

    @Override
    public void deleteRequestBuId(Long requestId) {

    }
}
