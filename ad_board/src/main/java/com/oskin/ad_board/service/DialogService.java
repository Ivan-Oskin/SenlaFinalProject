package com.oskin.ad_board.service;


import com.oskin.ad_board.model.Dialog;
import com.oskin.ad_board.repository.DialogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DialogService {
    private final DialogRepository dialogRepository;

    @Autowired
    public DialogService(DialogRepository DialogRepository){
        this.dialogRepository = DialogRepository;
    }

    public boolean save(Dialog dialog) {
        return dialogRepository.create(dialog);
    }

    public boolean update(Dialog Dialog) {
        return dialogRepository.update(Dialog);
    }

    public boolean delete(int id) {
        return dialogRepository.delete(id);
    }

    public Dialog findById(int id) {
        Optional<Dialog> optional = dialogRepository.findById(id);
        return optional.orElse(null);
    }
}
