const NewTodoElement = (index: number, forceUpdate: React.DispatchWithoutAction) => {
    const del = () => {
        forceUpdate();
    }

    return (
        <div key={index}
        className="w-full h-16 my-2 bg-gray-100 rounded-xl text-3xl flex items-center px-4">
            <div className="w-12 text-right">{index}</div>
            <div className="w-0.5 h-5/6 bg-gray-400 mx-3"/>
            <input type="text" />
            <div className="ml-auto select-none cursor-pointer"
            onClick={del}>❌</div>
        </div>
    )
}

export default NewTodoElement;