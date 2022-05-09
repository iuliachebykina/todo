import { ListElement, List } from "./interfaces/ListInterface";
import StorageProvider from "./StorageProvider";
import axios from 'axios';
import path from "../properties";


class Table {
    static list: List;
    static SelectedIndex: number|undefined;
    static Delete = async (id: ListElement["id"]) => {
        return deleteTodo(id);
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

export async function editTodo(id: number, task: string){
    return axios.put(path, {id: id, task: task});
}

export interface ToDo{
    id: number,
    task: string
}

interface ToDoData{
    toDoData: ToDo[]
}

export default DataProvider;