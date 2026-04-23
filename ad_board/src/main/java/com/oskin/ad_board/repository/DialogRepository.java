package com.oskin.ad_board.repository;

import com.oskin.ad_board.model.Dialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DialogRepository extends AbstractCrudRepository<Dialog> {

    @Autowired
    public DialogRepository() {
        super(Dialog.class);
    }
}
