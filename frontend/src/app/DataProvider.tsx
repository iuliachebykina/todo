import { ListElement, List } from "./interfaces/ListInterface";
import StorageProvider from "./StorageProvider";


class Table {
    static list: List;
    static SelectedIndex: number|undefined;
    static Get = () => Table.list;
    static Add = (element: ListElement|string) => {
        console.log(element);
        if (typeof element === "string")
            element = {description: element, id: Math.round(Math.random() * Number.MAX_SAFE_INTEGER)};

        Table.list.elements.push(element);
        StorageProvider.SaveData(Table.Get());
    }
    static Delete = (id: ListElement["id"]) => {
        Table.list.elements.splice(Table.list.elements.findIndex(x => x.id === id), 1);
        StorageProvider.SaveData(Table.Get());
    }

    static Init = (list?: List) => {
        Table.list = list ?? { elements: []};
    }
}

class DataProvider {
    static Table = Table;

    static Init = () => {
        StorageProvider.Init();
        Table.Init(StorageProvider.LoadData());
    } 
}

export default DataProvider;