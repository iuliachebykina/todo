import { ListElement, List } from "./interfaces/ListInterface";
import StorageProvider from "./StorageProvider";
import axios from 'axios';
import path from "../properties";


class Table {
    static list: List;
    static SelectedIndex: number|undefined;
    static Add = async (element: ListElement | string) => {
        if (typeof element === "string")
            element = {description: element, id: Math.round(Math.random() * Number.MAX_SAFE_INTEGER)};

        console.log(await addTodo(element.description));
    }
    static Delete = async (id: ListElement["id"]) => {
        console.log(deleteTodo(id));
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
    return axios.get<ToDo[]>(path);
}

export async function addTodo(element: string){
    return axios.post<ToDo>(path, {task: element});
}

export async function deleteTodo(id: number){
    return axios.delete(`${path}/${id}`);
}

interface ToDo{
    id: number,
    task: string
}

interface ToDoData{
    toDoData: ToDo[]
}

export default DataProvider;