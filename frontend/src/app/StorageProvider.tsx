import { List } from "./interfaces/ListInterface";
import { saveAs } from "file-saver";

import Papa from "papaparse";
import DataProvider from "./DataProvider";

enum StorageType {
    "localstorage"
}

interface Storage {
    LoadData(): List;
    SaveData(list :List): void;
}

class StorageProvider {
    static storageType = StorageType.localstorage;
    static storage?: Storage;

    static Init = () => {
        let localStorageType = localStorage.getItem("storageType");
        if (localStorageType !== null) {
            StorageProvider.storageType = StorageType[localStorageType as keyof typeof StorageType];
            switch (StorageProvider.storageType) {
            }
        }
        else {
            StorageProvider.storage = new localStorageProvider();
        }
    }

    static LoadData = () => StorageProvider.storage?.LoadData();
    static SaveData = (list: List) => StorageProvider.storage?.SaveData(list);

    static ImportData = (forceUpdate: ()=>any) => {
        let fileHandle = document.createElement("input") as HTMLInputElement;
        fileHandle.type = "file";
        fileHandle.onchange = () => {
            if (!fileHandle.files || !fileHandle.files[0]) return;
            let file = fileHandle.files[0];
            let reader = new FileReader();
            reader.readAsText(file, "UTF-8");
            reader.onload = (e) => {
                if (!StorageProvider.storage) return;
                if (!e.target?.result) return;
                let data = Papa.parse<string>(e.target.result.toString()).data.flat().filter(x => x);
                DataProvider.Table.list = {elements: data.map((desc, i) => {return {id: i, description: desc}})};
                StorageProvider.SaveData(DataProvider.Table.Get());
                forceUpdate();
            }
            reader.onerror = (e) => console.error(e);            
        }
        fileHandle.click();
    }

    static ExportData = () => {
        if (!StorageProvider.storage) return;
        let list = StorageProvider.storage?.LoadData();
        let csvText = Papa.unparse(list.elements.map(e => [e.description]));
        var blob = new Blob([csvText], {type: "text/csv;charset=utf-8"});
        saveAs(blob, "My RanTODOm list.csv");
    }
}

class localStorageProvider implements Storage {
    LoadData = () :List =>{
        return JSON.parse(localStorage.getItem("list") ?? "{ \"elements\": []}");
    }

    SaveData = (list: List) => {
        localStorage.setItem("list", JSON.stringify(list));
    }
}

export default StorageProvider;