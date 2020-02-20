package com.dailymission.api.springboot.batch;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

public class JpaItemListWriter<T> extends JpaItemWriter<List<T>> {
    private JpaItemWriter<T> jpaItemWriter;

    public JpaItemListWriter(JpaItemWriter<T> jpaItemWriter){
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items) {
        for(List<T> list: items){
            this.jpaItemWriter.write(list);
        }
    }
}
