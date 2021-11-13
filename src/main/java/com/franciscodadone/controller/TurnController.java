package com.franciscodadone.controller;

import com.franciscodadone.view.TurnView;

import javax.swing.*;

public class TurnController {

    public TurnController(TurnView view) {

        DefaultListModel defaultListModel = new DefaultListModel();

        view.productList.setModel(defaultListModel);
        view.productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca colaaaa");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca cola");
        defaultListModel.addElement("coca caaola");
        defaultListModel.addElement("coca colaaaa");


    }

}
