const Donate = ({shown, setShown: setShown, update}: {shown: boolean, setShown:(x:boolean)=>any, update: ()=>any}): JSX.Element => {
    if (!shown) return <></>
    return (
        <div className="z-10 w-full sm:w-96 h-fit border-2 rounded-lg bg-white fixed inset-1/2 transform -translate-x-1/2 -translate-y-1/2 p-4">
            <div className="absolute top-2 right-2 cursor-pointer" onClick={()=>setShown(false)}>❌</div>
            <div className="flex flex-col gap-2">
                <div className="text-4xl font-bold mx-auto text-center">Donation</div>
                <div className="text-xl flex justify-center items-center">
                    <div className="text-white font-bold self-center text-center w-48 text-base p-2 bg-blue-700 rounded-lg cursor-pointer"
                    onClick={() => window.open("https://ko-fi.com/osa413")}>Donate via Ko-fi</div>
                </div>
                <div className="text-xl flex justify-center items-center">
                    <div className="text-white font-bold self-center text-center w-48 text-base p-2 bg-blue-700 rounded-lg cursor-pointer"
                    onClick={() => window.open("https://boosty.to/osa413/single-payment/donation/96216?share=target_link")}>Donate via Boosty</div>
                </div>
                <hr/>
                <div className="mx-auto">
                    Thank you ♥
                </div>
            </div>
        </div>
    )
}

export default Donate;