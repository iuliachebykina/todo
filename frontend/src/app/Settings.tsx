import StorageProvider from "./StorageProvider";

const Settings = ({shown, setShown, update}: {shown: boolean, setShown:(x:boolean)=>any, update: ()=>any}): JSX.Element => {
    if (!shown) return <></>
    return (
        <div className="z-10 w-full sm:w-96 h-full sm:h-4/5 border-2 rounded-lg bg-white fixed inset-1/2 transform -translate-x-1/2 -translate-y-1/2 p-4">
            <div className="absolute top-2 right-2 cursor-pointer" onClick={()=>setShown(false)}>❌</div>
            <div className="flex flex-col gap-2">
                <div className="text-4xl font-bold mx-auto text-center">Data</div>
                <div className="text-xl flex justify-between items-center">
                    <div>Import CSV file</div>
                    <div className="text-white font-bold self-center text-center text-base p-2 bg-blue-700 rounded-lg cursor-pointer"
                    onClick={() => StorageProvider.ImportData(update)}>Select a file...</div>
                </div>
                <div className="text-xl flex justify-between items-center">
                    <div>Export CSV file</div>
                    <div className="text-white font-bold self-center text-center text-base p-2 bg-blue-700 rounded-lg cursor-pointer"
                    onClick={StorageProvider.ExportData}>Download CSV</div>
                </div>
                <hr/>
                <div className="mx-auto text-blue-800 underline"><a target="_blank" href="https://github.com/OSA413/RanTODOm">Visit Repository</a></div>
            </div>
        </div>
    )
}

export default Settings;