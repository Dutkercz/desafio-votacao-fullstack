import { Route, Routes } from 'react-router-dom'
import { Toaster } from 'sonner'
import HomePage from './pages/HomePage'
import { ResultPage } from './pages/ResultPage'
import { useState } from 'react'


export function App() {
  const [associateId, setAssociateId] = useState<number>(1) //inicia com o usuario de ID 1
  return (
    <>
      <Toaster />
      <Routes>
        <Route path='/' element={<HomePage associateId={associateId} setAssociateId={setAssociateId} />} />
        <Route path='/results' element={<ResultPage />} />
      </Routes>
    </>
  )
}

export default App
