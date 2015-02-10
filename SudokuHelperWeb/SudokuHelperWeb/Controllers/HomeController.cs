using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace SudokuHelperWeb.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            ViewBag.Title = "";

            return View();
        }

        public ActionResult Rules()
        {
            ViewBag.Title = "Rules of Sudoku";
            return View();
        }

        public ActionResult AppControls()
        {
            ViewBag.Title = "How to Use the App";
            return View();
        }

        public ActionResult Techniques()
        {
            ViewBag.Title = "Solving Techniques";
            return View();
        }

        public ActionResult Contact()
        {
            //ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}
