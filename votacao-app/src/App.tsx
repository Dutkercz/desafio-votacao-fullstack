import { Route, Routes } from 'react-router-dom'
import { Toaster } from 'sonner'
import HomePage from './pages/HomePage'
import { ResultPage } from './pages/ResultPage'


export function App() {
  return (
    <>
      <Toaster />
      <Routes>
        <Route path='/' element={<HomePage />} />
        <Route path='/results' element={<ResultPage />} />
      </Routes>
    </>
  )
}

export default App
