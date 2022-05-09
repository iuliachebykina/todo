import Overlay from "./overlay";
import TodoList from "./List/List";
import Header from "./header/header";
import { useReducer, useState } from "react";
import Settings from "./Settings";
import Donate from "./Donate";

const Main = () => {
    const [, forceUpdate] = useReducer(x => x + 1, 0);
    const [settingsVisible, setSettingsVisible] = useState(false);
    const [donateVisible, setDonateVisible] = useState(false);
    const [selectedIndex, setSelectedIndex] = useState(-1);
    const [currentListLength, setCurrentListLength] = useState(0);

    return (
        <div>
            <Donate shown={donateVisible} setShown={setDonateVisible} update={forceUpdate}/>
            <Settings shown={settingsVisible} setShown={setSettingsVisible} update={forceUpdate}/>
            <Overlay update={forceUpdate} setSettingsVisible={setSettingsVisible} currentListLength={currentListLength} setSelectedIndex={setSelectedIndex}/>
            <Header setShowSettings={setSettingsVisible} setShowDonate={setDonateVisible}/>
            <TodoList selectedIndex={selectedIndex} setCurrentListLength={setCurrentListLength}/>
        </div>
    )
}

export default Main