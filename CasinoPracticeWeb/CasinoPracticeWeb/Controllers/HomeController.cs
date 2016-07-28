using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace CasinoPracticeWeb.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Contact()
        {
            return View();
        }

        public ActionResult GetIt()
        {
            ViewBag.Title = "Casino Practice - Get It";
            ViewBag.HeaderTitle = "Get It!";
            return View();
        }
    }
}