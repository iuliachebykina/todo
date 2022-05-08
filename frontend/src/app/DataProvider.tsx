import { ListElement, List } from "./interfaces/ListInterface";
import StorageProvider from "./StorageProvider";
import axios from 'axios';


class Table {
    static list: List;
    static SelectedIndex: number|undefined;
    static Get = async () => await getAllTodos();
    static Add = async (element: ListElement | string) => {
        console.log(element);
        if (typeof element === "string")
            element = {description: element, id: Math.round(Math.random() * Number.MAX_SAFE_INTEGER)};

        Table.list.elements.push(element);
    }
    static Delete = async (id: ListElement["id"]) => {
        Table.list.elements.splice(Table.list.elements.findIndex(x => x.id === id), 1);
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

export async function getAllTodos() {
    return axios.get<ToDo[]>('http://localhost:8090/todo');
}

interface ToDo{
    id: number,
    task: string
}

interface ToDoData{
    toDoData: ToDo[]
}

export default DataProvider;