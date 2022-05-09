import React, {useEffect, useReducer, useState} from "react"
import DataProvider from "../DataProvider"
import { ListElement } from "../interfaces/ListInterface"
import {getAllTodos} from "../DataProvider";
import {deleteTodo} from "../DataProvider";

const TodoElement = (element: ListElement, index: number, forceUpdate: React.DispatchWithoutAction, selectedIndex: number) => {
    const del = () => {
        if (DataProvider.Table.SelectedIndex === index)
            DataProvider.Table.SelectedIndex = undefined;
        deleteTodo(index)
        forceUpdate();
    }
    console.log(selectedIndex);

    return (
        <div key={index}
            className={"w-full h-16 my-2 rounded-xl text-base sm:text-3xl flex items-center px-4 " +
            (selectedIndex === index ? "bg-gray-300" : "bg-gray-100")}>
            <div className="w-6 sm:w-12 text-right">{index + 1}</div>
            <div className="w-0.5 h-5/6 bg-gray-400 mx-1 sm:mx-3"/>
            <div className="w-fit">{element.description}</div>
            <div className="ml-auto select-none cursor-pointer"
            onClick={del}>❌</div>
        </div>
    )
}

const TodoList = ({setCurrentListLength, selectedIndex}: {setCurrentListLength: (length: number) => any,
    selectedIndex: number}) => {
    const [, forceUpdate] = useReducer(x => x + 1, 0);
    const [elements, setElements] = useState<JSX.Element[]>([]);
    useEffect(() => {
        getAllTodos().then(res => {
            let test = res.data.map(e => TodoElement({id: e.id, description: e.task}, e.id, forceUpdate, selectedIndex));
            setElements(test);
            setCurrentListLength(elements.length);
        });
    });

    return (
        <div className="px-2 sm:px-4 mb-20 sm:mb-24" id="todo-list">
            {elements}
        </div>
    )
}

export default TodoList;